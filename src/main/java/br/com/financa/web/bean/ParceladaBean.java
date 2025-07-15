package br.com.financa.web.bean;

import br.com.financa.web.dto.ContaParceladaResponseDto;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

@Named("parceladaBean")
@ViewScoped
@Getter
@Setter
public class ParceladaBean implements Serializable {

    private List<ContaParceladaResponseDto> lista = new ArrayList<>();
    private double totalDevedor;
    private int qtdAtivas;
    private int qtdFinalizadas;
    private LocalDate proximoVencimento;

    private final String API_BASE = "http://localhost:8080/contas-parceladas";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        carregarContas();
        calcularResumo();
    }

    public void carregarContas() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/usuario/" + usuarioId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            lista = objectMapper.readValue(response.body(), new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lista = new ArrayList<>();

            System.out.println("=== Contas retornadas ===");
            for (ContaParceladaResponseDto dto : lista) {
                System.out.println("-> " + dto.getDescricao() + " | status: " + dto.getStatus());
            }

        }
    }

    public void calcularResumo() {
        totalDevedor = lista.stream()
                .filter(c -> "ativa".equalsIgnoreCase(c.getStatus()))
                .mapToDouble(c -> c.getValorTotal() * (1 - (c.getPercentualPago() / 100.0)))
                .sum();

        qtdAtivas = (int) lista.stream().filter(c -> "ativa".equalsIgnoreCase(c.getStatus())).count();
        qtdFinalizadas = (int) lista.stream().filter(c -> "finalizada".equalsIgnoreCase(c.getStatus())).count();

        proximoVencimento = lista.stream()
                .map(ContaParceladaResponseDto::getProximoVencimento)
                .filter(d -> d != null && d.isAfter(LocalDate.now()))
                .sorted()
                .findFirst()
                .orElse(null);
    }

    public String getTotalDevedorFormatado() {
        return String.format("%.2f", totalDevedor);
    }

    public String getProximoVencimentoFormatado() {
        return proximoVencimento != null
                ? proximoVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "Nenhum";
    }


}
