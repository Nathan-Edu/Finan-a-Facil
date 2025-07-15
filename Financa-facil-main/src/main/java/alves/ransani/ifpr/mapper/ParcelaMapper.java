package alves.ransani.ifpr.mapper;

import alves.ransani.ifpr.dao.Parcela;
import alves.ransani.ifpr.dto.parcela.ParcelaResponseDto;

public class ParcelaMapper {

    public static ParcelaResponseDto toResponseDto(Parcela parcela) {
        ParcelaResponseDto dto = new ParcelaResponseDto();
        dto.setId(parcela.getId());
        dto.setNumero(parcela.getNumero());
        dto.setValor(parcela.getValor());
        dto.setDataVencimento(parcela.getDataVencimento());
        dto.setPaga(parcela.isPaga());
        return dto;
    }
}
