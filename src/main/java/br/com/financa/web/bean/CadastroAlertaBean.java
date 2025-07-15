package br.com.financa.web.bean;

import br.com.financa.web.dto.NovoAlertaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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

@Named
@ViewScoped
@Getter
@Setter
public class CadastroAlertaBean implements Serializable {

    private static final String API_URL = "http://localhost:8080/alertas";

    private NovoAlertaDto alerta;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Inject
    private LoginBean loginBean;

    public CadastroAlertaBean() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void init() {
        alerta = new NovoAlertaDto();
    }

    public void salvar() throws IOException {
        try {
            alerta.setUsuarioId(loginBean.getUsuario().getId());

            String json = objectMapper.writeValueAsString(alerta);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Alerta criado com sucesso!"));
                alerta = new NovoAlertaDto(); // limpa o formulário
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao criar alerta."));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao comunicar com o servidor."));
        }
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("alertas.xhtml");
    }
}
