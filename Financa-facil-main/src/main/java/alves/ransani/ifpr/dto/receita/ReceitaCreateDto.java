package alves.ransani.ifpr.dto.receita;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaCreateDto {
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private String categoria;
    private Long usuarioId;
}
