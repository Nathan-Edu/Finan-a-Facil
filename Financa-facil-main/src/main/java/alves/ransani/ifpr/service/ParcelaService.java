package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dto.parcela.ParcelaResponseDto;
import alves.ransani.ifpr.mapper.ParcelaMapper;
import alves.ransani.ifpr.repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParcelaService {

    @Autowired
    private ParcelaRepository parcelaRepository;

    public List<ParcelaResponseDto> listarTodas() {
        return parcelaRepository.findAll()
                .stream()
                .map(ParcelaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ParcelaResponseDto> buscarPorId(Long id) {
        return parcelaRepository.findById(id)
                .map(ParcelaMapper::toResponseDto);
    }

    public boolean marcarComoPaga(Long id) {
        return parcelaRepository.findById(id).map(parcela -> {
            parcela.setPaga(true);
            parcelaRepository.save(parcela);
            return true;
        }).orElse(false);
    }

    public List<ParcelaResponseDto> listarPorContaParcelada(Long contaId) {
        return parcelaRepository.findAll().stream()
                .filter(p -> p.getContaParcelada() != null && p.getContaParcelada().getId().equals(contaId))
                .map(ParcelaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
