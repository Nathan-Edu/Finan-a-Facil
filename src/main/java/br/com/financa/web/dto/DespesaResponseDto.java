package br.com.financa.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
public class DespesaResponseDto {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private String categoria;
    private Long usuarioId;

    public String getValorFormatado() {
        return "R$ " + (valor != null ? valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "0,00");
    }


    public String getCorCategoria() {
        return switch (categoria != null ? categoria.toUpperCase() : "") {
            case "MORADIA" -> "#fee2e2";
            case "ALIMENTACAO" -> "#fef3c7";
            case "SAUDE" -> "#d1fae5";
            case "TRANSPORTE" -> "#e0f2fe";
            default -> "#ede9fe";
        };
    }

    public String getNomeCategoria() {
        return switch (categoria != null ? categoria.toUpperCase() : "") {
            case "MORADIA" -> "Moradia";
            case "ALIMENTACAO" -> "Alimentação";
            case "SAUDE" -> "Saúde";
            case "TRANSPORTE" -> "Transporte";
            default -> "Outros";
        };
    }

    public String getFormaPagamento() {
        return "Cartão";
    }
}
