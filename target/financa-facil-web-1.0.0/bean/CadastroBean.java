package br.com.financafacil.web.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Objects;

@Named("cadastroBean")
@RequestScoped
public class CadastroBean implements Serializable {

    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;
    private String erro;

    public String registrar() {
        // Verificações básicas
        if (nome == null || email == null || senha == null || confirmarSenha == null ||
                nome.isBlank() || email.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
            erro = "Preencha todos os campos.";
            return null;
        }

        if (!senha.equals(confirmarSenha)) {
            erro = "As senhas não coincidem.";
            return null;
        }

        // Simulando sucesso no cadastro
        erro = null;
        // Aqui você pode salvar o usuário no banco ou serviço real
        return "/pages/login.xhtml?faces-redirect=true";
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getConfirmarSenha() { return confirmarSenha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }

    public String getErro() { return erro; }
    public void setErro(String erro) { this.erro = erro; }
}