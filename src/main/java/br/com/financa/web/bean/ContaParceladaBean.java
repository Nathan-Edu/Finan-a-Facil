package br.com.financa.web.bean;

import br.com.financa.web.dto.ContaParceladaResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@Named
@RequestScoped
@Getter
@Setter
public class ContaParceladaBean implements Serializable {

    private String descricao;
    private BigDecimal valorTotal;
    private Integer quantidadeParcelas;
    private LocalDate dataInicio;

    private String erro;

    private final String API_URL = "http://localhost:8080/contas-parceladas";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    private LoginBean loginBean;

    private List<ContaParceladaResponseDto> lista;
    private double totalDevedor;
    private int qtdAtivas;
    private int qtdFinalizadas;
    private String proximoVencimentoFormatado;

    @PostConstruct
    public void init() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String salvar() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();

            String json = String.format("""
            {
                "descricao": "%s",
                "valorTotal": %s,
                "quantidadeParcelas": %d,
                "dataInicio": "%s",
                "usuarioId": %d
            }
        """, descricao, valorTotal.toString().replace(",", "."), quantidadeParcelas, dataInicio, usuarioId);

            System.out.println("JSON enviado: " + json); // DEBUG

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode()); // DEBUG
            System.out.println("Body: " + response.body());         // DEBUG

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return "/pages/parceladas.xhtml?faces-redirect=true";
            } else {
                erro = "Erro ao salvar conta parcelada. Código: " + response.statusCode();
                return null;
            }

        } catch (Exception e) {
            erro = "Erro interno ao salvar conta parcelada: " + e.getMessage();
            return null;
        }
    }
}

