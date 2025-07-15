package alves.ransani.ifpr.dto.conta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaParceladaCreateDto {
    private String descricao;
    private BigDecimal valorTotal;
    private int quantidadeParcelas;
    private LocalDate dataInicio;
    private Long usuarioId;
}
