package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.Receita;
import alves.ransani.ifpr.dto.receita.ReceitaCreateDto;
import alves.ransani.ifpr.dto.receita.ReceitaResponseDto;
import alves.ransani.ifpr.mapper.ReceitaMapper;
import alves.ransani.ifpr.repository.ReceitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public ReceitaResponseDto criarReceita(ReceitaCreateDto dto) {
        Receita receita = ReceitaMapper.toEntity(dto);
        receita = receitaRepository.save(receita);
        return ReceitaMapper.toResponseDto(receita);
    }

    public List<ReceitaResponseDto> listarTodas() {
        return receitaRepository.findAll()
                .stream()
                .map(ReceitaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ReceitaResponseDto> buscarPorId(Long id) {
        return receitaRepository.findById(id)
                .map(ReceitaMapper::toResponseDto);
    }
    public Optional<ReceitaResponseDto> atualizarReceita(Long id, ReceitaCreateDto dto) {
        Optional<Receita> optional = receitaRepository.findById(id);
        if (optional.isEmpty()) return Optional.empty();

        Receita receita = optional.get();
        receita.setDescricao(dto.getDescricao());
        receita.setValor(dto.getValor());
        receita.setData(dto.getData());
        receita.setCategoria(dto.getCategoria());
        receita = receitaRepository.save(receita);

        return Optional.of(ReceitaMapper.toResponseDto(receita));
    }


    public boolean deletarReceita(Long id) {
        if (receitaRepository.existsById(id)) {
            receitaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ReceitaResponseDto> buscarPorUsuario(Long usuarioId) {
        return receitaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ReceitaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
