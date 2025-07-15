package alves.ransani.ifpr.dto.receita;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ReceitaResponseDto {

    private Long id;
    private String descricao;
    private BigDecimal valor;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    private String categoria;


}
