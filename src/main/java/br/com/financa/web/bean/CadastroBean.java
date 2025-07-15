package br.com.financa.web.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Named("cadastroBean")
@RequestScoped
@Getter
@Setter
public class CadastroBean implements Serializable {

    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;
    private String erro;

    private final String API_URL = "http://localhost:8080/usuarios/cadastro";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String registrar() {
        try {
            // Validações
            if (nome == null || email == null || senha == null || confirmarSenha == null ||
                    nome.isBlank() || email.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
                erro = "Todos os campos são obrigatórios.";
                return null;
            }

            if (!senha.equals(confirmarSenha)) {
                erro = "As senhas não coincidem.";
                return null;
            }

            // Constrói JSON com segurança
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("nome", nome);
            jsonMap.put("email", email);
            jsonMap.put("senha", senha);

            String json = mapper.writeValueAsString(jsonMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return "/pages/login.xhtml";
            } else if (response.statusCode() == 400 && response.body().contains("E-mail já cadastrado")) {
                erro = "Este e-mail já está em uso.";
            } else {
                erro = "Erro ao cadastrar. Código: " + response.statusCode() + "\n" + response.body();
            }

        } catch (Exception e) {
            erro = "Erro interno: " + e.getMessage();
        }

        return null;
    }
}
