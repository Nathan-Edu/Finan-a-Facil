package alves.ransani.ifpr.dto.conta;

import alves.ransani.ifpr.dto.parcela.ParcelaResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaParceladaResponseDto {
    private Long id;
    private String descricao;
    private double valorTotal;
    private double valorParcela;
    private int parcelas;
    private int parcelasPagas;
    private String status; // ativa / finalizada
    private LocalDate proximoVencimento;
    private int percentualPago;

    private List<ParcelaResponseDto> listaParcelas;
}
