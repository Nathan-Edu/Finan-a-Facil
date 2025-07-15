package br.com.financa.web.bean;

import br.com.financa.web.dto.ReceitaCreateDto;
import br.com.financa.web.dto.ReceitaResponseDto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class ReceitasNovaBean implements Serializable {

    private Long id;
    private String descricao;
    private String categoria;
    private Double valor;
    private LocalDate data;

    private final String API_BASE = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public ReceitasNovaBean() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        if (params.containsKey("id")) {
            try {
                this.id = Long.valueOf(params.get("id"));
            } catch (NumberFormatException ignored) {}
        }
    }

    public void preRender(ComponentSystemEvent event) {
        if (id != null) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/receitas/" + id))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                ReceitaResponseDto receita = objectMapper.readValue(response.body(), ReceitaResponseDto.class);
                this.descricao = receita.getDescricao();
                this.categoria = receita.getCategoria();
                this.valor = receita.getValor();
                this.data = receita.getData();

            } catch (IOException | InterruptedException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar receita para edição"));
            }
        }
    }

    public void salvar() {
        try {
            ReceitaCreateDto dto = new ReceitaCreateDto();
            dto.setDescricao(descricao);
            dto.setCategoria(categoria);
            dto.setValor(valor);
            dto.setData(data);
            dto.setUsuarioId(loginBean.getUsuario().getId());

            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request;

            if (id != null) {
                // Atualizar receita
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/receitas/" + id))
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                // Nova receita
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/receitas"))
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .header("Content-Type", "application/json")
                        .build();
            }

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("receitas.xhtml");
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar receita"));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar receita"));
        }
    }
}
