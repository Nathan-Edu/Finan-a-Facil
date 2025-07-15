package alves.ransani.ifpr.dto.despesa;

import alves.ransani.ifpr.dao.Despesa;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaCreateDto {
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Despesa.Categoria categoria;
    private Long usuarioId;
}
