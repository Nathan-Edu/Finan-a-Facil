package alves.ransani.ifpr.dto.alerta;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlertaResponseDto {
    private Long id;
    private String mensagem;
    private LocalDate dataAlerta;
    private boolean visualizado;
}
