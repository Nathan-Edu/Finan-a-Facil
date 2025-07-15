package br.com.financa.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReceitaCreateDto {
    private String descricao;
    private double valor;
    private String categoria;
    private LocalDate data;
    private Long usuarioId;
}
