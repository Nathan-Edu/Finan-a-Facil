package br.com.financa.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaResponseDto {
    private Long id;
    private String descricao;
    private String categoria;
    private LocalDate data;
    private Double valor;
    private Long usuarioId;


    public String getDataFormatada() {
        return data != null ? data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public String getValorFormatado() {
        return valor != null ? String.format("R$ %.2f", valor) : "R$ 0,00";
    }
}
