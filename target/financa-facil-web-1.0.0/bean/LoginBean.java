package br.com.financafacil.web.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Objects;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String email;
    private String senha;
    private String erro;

    public String logar() {
        // Simulação de login (ajuste para validação real depois)
        if (Objects.equals(email, "usuario@teste.com") && Objects.equals(senha, "123456")) {
            erro = null;
            return "/pages/dashboard.xhtml?faces-redirect=true";
        } else {
            erro = "E-mail ou senha inválidos.";
            return null; // Mantém na mesma página
        }
    }

    public String logout() {
        email = null;
        senha = null;
        erro = null;
        return "/index.xhtml?faces-redirect=true";
    }

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getErro() { return erro; }
    public void setErro(String erro) { this.erro = erro; }
}