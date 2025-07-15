package br.com.financa.web.bean;

import br.com.financa.web.dto.UsuarioResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Named("loginBean")
@SessionScoped
@Getter
@Setter
public class LoginBean implements Serializable {

    private String email;
    private String senha;
    private String erro;

    private UsuarioResponseDto usuario;

    private final String API_URL = "http://localhost:8080/usuarios/login";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String logar() {
        try {
            if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
                erro = "Informe email e senha.";
                return null;
            }

            String json = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode node = mapper.readTree(response.body());
                usuario = new UsuarioResponseDto();
                usuario.setId(node.get("id").asLong());
                usuario.setNome(node.get("nome").asText());
                usuario.setEmail(node.get("email").asText());

                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("usuarioId", usuario.getId());

                return "/pages/dashboard.xhtml";
            } else {
                erro = "Email ou senha inválidos.";
                return null;
            }

        } catch (Exception e) {
            erro = "Erro na conexão com o servidor.";
            return null;
        }
    }

    public String logout() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().invalidateSession();
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/pages/index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // redirecionamento já foi feito manualmente
    }


}
