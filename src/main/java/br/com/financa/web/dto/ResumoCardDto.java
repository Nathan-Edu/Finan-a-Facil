package br.com.financa.web.dto;

public class ResumoCardDto {
    private String titulo;
    private String valor;
    private String info;
    private String cor;

    public ResumoCardDto(String titulo, String valor, String info, String cor) {
        this.titulo = titulo;
        this.valor = valor;
        this.info = info;
        this.cor = cor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getValor() {
        return valor;
    }

    public String getInfo() {
        return info;
    }

    public String getCor() {
        return cor;
    }
}
