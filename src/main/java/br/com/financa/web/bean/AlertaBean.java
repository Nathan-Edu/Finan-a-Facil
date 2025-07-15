package br.com.financa.web.bean;

import br.com.financa.web.dto.AlertaResponseDto;
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

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class AlertaBean implements Serializable {

    private List<AlertaResponseDto> lista = new ArrayList<>();
    private int qtdNaoVisualizados;
    private int qtdVisualizados;
    private int qtdAltaPrioridade;
    private int total;

    private final String API_BASE = "http://localhost:8080/alertas";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    @Inject
    private LoginBean loginBean;

    public AlertaBean() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    public void init() {
        carregarAlertas();
        contarEstatisticas();
    }

    public void carregarAlertas() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/usuario/" + usuarioId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("=== Alerta JSON ===\n" + response.body());

            lista = objectMapper.readValue(response.body(), new TypeReference<>() {});
            System.out.println("=== Lista carregada ===\n" + lista);

            formatarDatas();
            calcularPrioridades();
        } catch (Exception e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }
    }

    private void formatarDatas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lista.forEach(a -> {
            if (a.getDataAlerta() != null) {
                a.setDataFormatada(a.getDataAlerta().format(formatter));
            }
        });
    }

    private void calcularPrioridades() {
        lista.forEach(a -> {
            if (a.getMensagem().toLowerCase().contains("vence")) {
                a.setPrioridade("high");
            } else {
                a.setPrioridade("normal");
            }
        });
    }

    public void contarEstatisticas() {
        total = lista.size();
        qtdNaoVisualizados = (int) lista.stream().filter(a -> !a.isVisualizado()).count();
        qtdVisualizados = total - qtdNaoVisualizados;
        qtdAltaPrioridade = (int) lista.stream().filter(a -> "high".equals(a.getPrioridade())).count();
    }

    public void marcarComoLido(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/" + id + "/visualizar"))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            carregarAlertas();
            contarEstatisticas();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void descartar(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/" + id))
                    .DELETE()
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            carregarAlertas();
            contarEstatisticas();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
