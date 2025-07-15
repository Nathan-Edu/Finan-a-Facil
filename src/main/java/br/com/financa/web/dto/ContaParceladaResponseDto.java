package br.com.financa.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ContaParceladaResponseDto {
    private Long id;
    private String descricao;
    private double valorTotal;
    private double valorParcela;
    private int parcelas;
    private int parcelasPagas;
    private String status; // "ativa" ou "finalizada"
    private LocalDate proximoVencimento;
    private int percentualPago;

    private List<ParcelaResponseDto> listaParcelas;

    public String getValorTotalFormatado() {
        return String.format("R$ %.2f", valorTotal);
    }

    public String getValorParcelaFormatado() {
        return String.format("R$ %.2f", valorParcela);
    }
}
