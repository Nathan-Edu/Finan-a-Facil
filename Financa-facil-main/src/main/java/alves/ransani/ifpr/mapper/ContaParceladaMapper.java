package alves.ransani.ifpr.mapper;

import alves.ransani.ifpr.dao.ContaParcelada;
import alves.ransani.ifpr.dao.Parcela;
import alves.ransani.ifpr.dto.conta.ContaParceladaCreateDto;
import alves.ransani.ifpr.dto.conta.ContaParceladaResponseDto;
import alves.ransani.ifpr.dto.parcela.ParcelaResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class ContaParceladaMapper {

    public static ContaParcelada toEntity(ContaParceladaCreateDto dto) {
        ContaParcelada conta = new ContaParcelada();
        conta.setDescricao(dto.getDescricao());
        conta.setValorTotal(dto.getValorTotal());
        conta.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        conta.setDataInicio(dto.getDataInicio());
        return conta;
    }

    public static ContaParceladaResponseDto toResponseDto(ContaParcelada conta) {
        ContaParceladaResponseDto dto = new ContaParceladaResponseDto();
        dto.setId(conta.getId());
        dto.setDescricao(conta.getDescricao());
        dto.setValorTotal(conta.getValorTotal().doubleValue());
        dto.setParcelas(conta.getQuantidadeParcelas());

        List<Parcela> parcelas = conta.getParcelas();
        if (parcelas == null || parcelas.isEmpty()) {
            dto.setParcelasPagas(0);
            dto.setStatus("ativa");
            dto.setValorParcela(0.0);
            dto.setPercentualPago(0);
            dto.setListaParcelas(List.of()); // garantir lista vazia
            return dto;
        }

        int pagas = (int) parcelas.stream().filter(Parcela::isPaga).count();
        dto.setParcelasPagas(pagas);

        dto.setStatus(pagas >= conta.getQuantidadeParcelas() ? "finalizada" : "ativa");

        double valorParcela = conta.getValorTotal().doubleValue() / conta.getQuantidadeParcelas();
        dto.setValorParcela(valorParcela);

        parcelas.stream()
                .filter(p -> !p.isPaga())
                .map(Parcela::getDataVencimento)
                .sorted()
                .findFirst()
                .ifPresent(dto::setProximoVencimento);

        int percentual = (int) (((double) pagas / conta.getQuantidadeParcelas()) * 100);
        dto.setPercentualPago(percentual);

        // Novo trecho: preencher listaParcelas
        List<ParcelaResponseDto> listaParcelas = parcelas.stream().map(p -> {
            ParcelaResponseDto pDto = new ParcelaResponseDto();
            pDto.setId(p.getId());
            pDto.setValor(p.getValor());
            pDto.setNumero(p.getNumero());
            pDto.setDataVencimento(p.getDataVencimento());
            pDto.setPaga(p.isPaga());
            return pDto;
        }).collect(Collectors.toList());

        dto.setListaParcelas(listaParcelas);

        return dto;
    }
}
