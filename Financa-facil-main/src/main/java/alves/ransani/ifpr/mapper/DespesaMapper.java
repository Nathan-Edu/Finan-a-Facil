package alves.ransani.ifpr.mapper;

import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dao.Despesa;
import alves.ransani.ifpr.dto.despesa.DespesaCreateDto;
import alves.ransani.ifpr.dto.despesa.DespesaResponseDto;

public class DespesaMapper {
    public static Despesa toEntity(DespesaCreateDto dto) {
        Despesa despesa = new Despesa();
        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setData(dto.getData());
        despesa.setCategoria(dto.getCategoria());
        return despesa;
    }

    public static DespesaResponseDto toResponseDto(Despesa despesa) {
        DespesaResponseDto dto = new DespesaResponseDto();
        dto.setId(despesa.getId());
        dto.setDescricao(despesa.getDescricao());
        dto.setValor(despesa.getValor());
        dto.setData(despesa.getData());
        dto.setCategoria(despesa.getCategoria());
        dto.setUsuarioId(despesa.getUsuario() != null ? despesa.getUsuario().getId() : null);
        return dto;
    }
}
