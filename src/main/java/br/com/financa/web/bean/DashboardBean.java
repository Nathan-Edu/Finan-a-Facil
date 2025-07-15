package br.com.financa.web.bean;

import br.com.financa.web.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class DashboardBean implements Serializable {

    private double saldo;
    private double totalReceitas;
    private double totalDespesas;
    private int contasParceladas;

    private List<ReceitaResponseDto> receitasRecentes;
    private List<DespesaResponseDto> despesasRecentes;
    private List<AlertaResponseDto> alertas;

    private final String API_BASE = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        if (loginBean.getUsuario() != null) {
            Long usuarioId = loginBean.getUsuario().getId();
            carregarResumoFinanceiro(usuarioId);
            carregarAlertas(usuarioId);
        }
    }

    private void carregarResumoFinanceiro(Long usuarioId) {
        try {
            // Receitas
            List<ReceitaResponseDto> receitas = getListFromApi(
                    "/receitas/usuario/" + usuarioId,
                    new TypeReference<>() {}
            );
            totalReceitas = receitas.stream()
                    .filter(r -> isDataDoMesAtual(r.getData()))
                    .mapToDouble(r -> r.getValor().doubleValue())
                    .sum();

            // Despesas
            List<DespesaResponseDto> despesas = getListFromApi(
                    "/despesas/usuario/" + usuarioId,
                    new TypeReference<>() {}
            );
            totalDespesas = despesas.stream()
                    .filter(d -> isDataDoMesAtual(d.getData()))
                    .mapToDouble(d -> d.getValor().doubleValue())
                    .sum();

            // Contas Parceladas
            List<ContaParceladaResponseDto> contas = getListFromApi(
                    "/contas-parceladas/usuario/" + usuarioId,
                    new TypeReference<>() {}
            );
            contasParceladas = (int) contas.stream()
                    .filter(c -> "ativa".equalsIgnoreCase(c.getStatus()))
                    .count();

            double totalParcelasMesAtual = contas.stream()
                    .flatMap(c -> c.getListaParcelas().stream())
                    .filter(p -> !p.isPaga() && isDataDoMesAtual(p.getDataVencimento()))
                    .mapToDouble(p -> p.getValor().doubleValue())
                    .sum();

            saldo = totalReceitas - totalDespesas - totalParcelasMesAtual;

            receitasRecentes = receitas.stream()
                    .sorted(Comparator.comparing(ReceitaResponseDto::getData).reversed())
                    .limit(3)
                    .collect(Collectors.toList());

            despesasRecentes = despesas.stream()
                    .sorted(Comparator.comparing(DespesaResponseDto::getData).reversed())
                    .limit(3)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            receitasRecentes = new ArrayList<>();
            despesasRecentes = new ArrayList<>();
            saldo = 0;
            totalDespesas = 0;
            totalReceitas = 0;
            contasParceladas = 0;
        }
    }

    private void carregarAlertas(Long usuarioId) {
        try {
            alertas = getListFromApi("/alertas/usuario/" + usuarioId, new TypeReference<>() {});

            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat saida = new SimpleDateFormat("dd/MM/yyyy");

            for (AlertaResponseDto alerta : alertas) {
                if (alerta.getPrioridade() == null && alerta.getMensagem() != null) {
                    if (alerta.getMensagem().toLowerCase().contains("vence")) {
                        alerta.setPrioridade("high");
                    } else {
                        alerta.setPrioridade("medium");
                    }
                }

                if (alerta.getDataAlerta() != null) {
                    try {
                        Date data = entrada.parse(alerta.getDataAlerta().toString());
                        alerta.setDataFormatada(saida.format(data));
                    } catch (Exception e) {
                        alerta.setDataFormatada("Data inválida");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            alertas = new ArrayList<>();
        }
    }

    private boolean isDataDoMesAtual(LocalDate data) {
        return data != null &&
                data.getMonthValue() == LocalDate.now().getMonthValue() &&
                data.getYear() == LocalDate.now().getYear();
    }

    private <T> List<T> getListFromApi(String path, TypeReference<List<T>> typeRef) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + path))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), typeRef);
    }

    public List<Map<String, Object>> getResumoCards() {
        List<Map<String, Object>> cards = new ArrayList<>();

        cards.add(Map.of("titulo", "Saldo Atual", "valor", String.format("R$ %.2f", saldo), "info", "+2.5% desde o mês passado", "cor", "azul"));
        cards.add(Map.of("titulo", "Receitas", "valor", String.format("R$ %.2f", totalReceitas), "info", "Este mês", "cor", "verde"));
        cards.add(Map.of("titulo", "Despesas", "valor", String.format("R$ %.2f", totalDespesas), "info", "Este mês", "cor", "vermelho"));
        cards.add(Map.of("titulo", "Contas Parceladas", "valor", String.valueOf(contasParceladas), "info", "Ativas", "cor", "roxo"));

        return cards;
    }

    public List<Map<String, Object>> getTransacoesRecentes() {
        List<Map<String, Object>> transacoes = new ArrayList<>();

        for (ReceitaResponseDto r : receitasRecentes) {
            transacoes.add(Map.of(
                    "descricao", r.getDescricao(),
                    "data", r.getData(),
                    "valor", r.getValor(),
                    "tipo", "receita",
                    "valorFormatado", String.format("+ R$ %.2f", r.getValor())
            ));
        }

        for (DespesaResponseDto d : despesasRecentes) {
            transacoes.add(Map.of(
                    "descricao", d.getDescricao(),
                    "data", d.getData(),
                    "valor", d.getValor(),
                    "tipo", "despesa",
                    "valorFormatado", String.format("- R$ %.2f", d.getValor())
            ));
        }

        return transacoes.stream()
                .sorted((a, b) -> ((LocalDate) b.get("data")).compareTo((LocalDate) a.get("data")))
                .limit(5)
                .collect(Collectors.toList());
    }
}
