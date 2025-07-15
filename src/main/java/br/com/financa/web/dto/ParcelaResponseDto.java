package br.com.financa.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ParcelaResponseDto {

    private Long id;
    private BigDecimal valor;
    private int numero;
    private LocalDate dataVencimento;
    private boolean paga;
    private Long contaParceladaId;

}
