package br.com.financa.web.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponseDto {
    private Long id;
    private String mensagem;
    private LocalDate dataAlerta;
    private boolean visualizado;
    private String prioridade;
    private String dataFormatada;
}
