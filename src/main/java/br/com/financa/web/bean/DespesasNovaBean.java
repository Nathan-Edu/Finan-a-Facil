package br.com.financa.web.bean;

import br.com.financa.web.dto.DespesaResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@Named
@ViewScoped
@Getter
@Setter
public class DespesasNovaBean implements Serializable {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private String categoria;
    private LocalDate data;
    private String erro;
    private boolean despesaFixa = false;

    private final String API_URL = "http://localhost:8080/api/despesas";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        var context = FacesContext.getCurrentInstance();
        String param = context.getExternalContext().getRequestParameterMap().get("id");

        if (param != null && !param.isBlank()) {
            this.id = Long.parseLong(param);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                DespesaResponseDto dto = mapper.readValue(response.body(), DespesaResponseDto.class);
                this.descricao = dto.getDescricao();
                this.valor = dto.getValor();
                this.categoria = dto.getCategoria();
                this.data = dto.getData();
            }
        }
    }

    public String salvar() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();

            String json = String.format("""
                {
                    "descricao": "%s",
                    "valor": "%s",
                    "categoria": "%s",
                    "data": "%s",
                    "usuarioId": %d
                }
            """, descricao, valor.toPlainString(), categoria, data, usuarioId);

            HttpRequest request;
            if (id != null) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + "/" + id))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            } else {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            }

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return "/pages/despesas.xhtml?faces-redirect=true";
            } else {
                erro = "Erro ao salvar despesa. Código: " + response.statusCode();
                return null;
            }

        } catch (Exception e) {
            erro = "Erro interno ao salvar despesa: " + e.getMessage();
            return null;
        }
    }
}
