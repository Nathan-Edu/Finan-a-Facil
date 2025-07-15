package alves.ransani.ifpr.dto.alerta;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertaCreateDto {
    private String mensagem;
    private LocalDate dataGeracao;
    private Long usuarioId;
}
