package alves.ransani.ifpr.dto.parcela;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParcelaResponseDto {
    private Long id;
    private BigDecimal valor;
    private int numero;
    private LocalDate dataVencimento;
    private boolean paga;
}
