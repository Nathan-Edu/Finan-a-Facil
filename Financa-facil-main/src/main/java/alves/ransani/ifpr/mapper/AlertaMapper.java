package alves.ransani.ifpr.mapper;

import alves.ransani.ifpr.dao.Alerta;
import alves.ransani.ifpr.dto.alerta.AlertaResponseDto;

public class AlertaMapper {

    public static AlertaResponseDto toResponseDto(Alerta alerta) {
        AlertaResponseDto dto = new AlertaResponseDto();
        dto.setId(alerta.getId());
        dto.setMensagem(alerta.getMensagem());
        dto.setDataAlerta(alerta.getDataGeracao());
        dto.setVisualizado(alerta.isVisualizado());
        return dto;
    }
}
