package br.com.financa.web.bean;

import br.com.financa.web.dto.ReceitaResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class ReceitasBean implements Serializable {

    private List<ReceitaResponseDto> lista = new ArrayList<>();
    private List<ReceitaResponseDto> listaFiltrada = new ArrayList<>();

    private double total;
    private double totalMesAtual;
    private int qtdFontes;
    private String filtroDescricao;

    private final String API_BASE = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public ReceitasBean() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Suporte para LocalDate
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Inject
    private LoginBean loginBean;

    public void carregarReceitas() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/receitas/usuario/" + usuarioId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            lista = objectMapper.readValue(response.body(), new TypeReference<>() {});
            listaFiltrada = new ArrayList<>(lista); // começa igual
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            listaFiltrada = new ArrayList<>();
        }
    }

    public void aplicarResumo() {
        total = listaFiltrada.stream()
                .mapToDouble(ReceitaResponseDto::getValor)
                .sum();

        totalMesAtual = listaFiltrada.stream()
                .filter(r -> r.getData() != null &&
                        r.getData().getMonthValue() == LocalDate.now().getMonthValue() &&
                        r.getData().getYear() == LocalDate.now().getYear())
                .mapToDouble(ReceitaResponseDto::getValor)
                .sum();

        qtdFontes = (int) listaFiltrada.stream()
                .map(ReceitaResponseDto::getDescricao)
                .distinct()
                .count();
    }

    public void filtrar() {
        if (filtroDescricao == null || filtroDescricao.trim().isEmpty()) {
            listaFiltrada = new ArrayList<>(lista);
        } else {
            String termo = filtroDescricao.toLowerCase();
            listaFiltrada = lista.stream()
                    .filter(r -> r.getDescricao().toLowerCase().contains(termo))
                    .toList();
        }
    }

    public String getTotalFormatado() {
        double total = lista.stream()
                .mapToDouble(ReceitaResponseDto::getValor)
                .sum();

        // Formata no padrão brasileiro: 1.234,56
        return String.format("R$ %,.2f", total)
                .replace('.', '#')   // Temporariamente troca . por #
                .replace(',', '.')   // Troca , por .
                .replace('#', ',');  // Troca # por ,
    }

    public String getTotalMesAtualFormatado() {
        return String.format("%.2f", totalMesAtual);
    }

    public int getQtdFontes() {
        return qtdFontes;
    }

    @PostConstruct
    public void init() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/receitas/usuario/" + usuarioId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            lista = objectMapper.readValue(response.body(), new TypeReference<List<ReceitaResponseDto>>() {});
            listaFiltrada = new ArrayList<>(lista); // <- Preenche listaFiltrada para exibir

            total = lista.stream()
                    .mapToDouble(ReceitaResponseDto::getValor)
                    .sum();

            totalMesAtual = lista.stream()
                    .filter(r -> r.getData() != null && r.getData().getMonthValue() == LocalDate.now().getMonthValue())
                    .mapToDouble(ReceitaResponseDto::getValor)
                    .sum();

            qtdFontes = (int) lista.stream()
                    .map(ReceitaResponseDto::getCategoria)
                    .distinct()
                    .count();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            listaFiltrada = new ArrayList<>();
        }
    }
    public void excluir(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/receitas/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                lista.removeIf(r -> r.getId().equals(id));
                listaFiltrada.removeIf(r -> r.getId().equals(id));
                aplicarResumo();

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Receita excluída com sucesso"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao excluir receita"));
            }

        } catch (IOException | InterruptedException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro de comunicação com o servidor"));
            e.printStackTrace();
        }
    }

}
