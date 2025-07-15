package alves.ransani.ifpr.dto.alerta;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NovoAlertaDto {
    private String mensagem;
    private LocalDate dataAlerta;
    private Long usuarioId;
}
