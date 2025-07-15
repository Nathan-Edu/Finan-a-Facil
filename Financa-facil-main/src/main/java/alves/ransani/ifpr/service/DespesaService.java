package alves.ransani.ifpr.service;
import alves.ransani.ifpr.dao.Despesa;
import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.despesa.DespesaCreateDto;
import alves.ransani.ifpr.dto.despesa.DespesaResponseDto;
import alves.ransani.ifpr.mapper.DespesaMapper;
import alves.ransani.ifpr.repository.DespesaRepository;
import alves.ransani.ifpr.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DespesaService {
    @Autowired
    private DespesaRepository despesaRepository;

    public List<DespesaResponseDto> listarTodas() {
        return despesaRepository.findAll()
                .stream()
                .map(DespesaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<DespesaResponseDto> buscarPorId(Long id) {
        return despesaRepository.findById(id)
                .map(DespesaMapper::toResponseDto);
    }

    public Optional<DespesaResponseDto> atualizarDespesa(Long id, DespesaCreateDto dto) {
        return despesaRepository.findById(id).map(despesaExistente -> {
            despesaExistente.setDescricao(dto.getDescricao());
            despesaExistente.setValor(dto.getValor());
            despesaExistente.setData(dto.getData());
            despesaExistente.setCategoria(dto.getCategoria());

            // Adicionado: garantir que o vínculo com o usuário seja mantido
            if (dto.getUsuarioId() != null) {
                Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId()));
                despesaExistente.setUsuario(usuario);
            }

            Despesa atualizada = despesaRepository.save(despesaExistente);
            return DespesaMapper.toResponseDto(atualizada);
        });


}

    public boolean deletarDespesa(Long id) {
        return despesaRepository.findById(id).map(despesa -> {
            despesaRepository.delete(despesa);
            return true;
        }).orElse(false);
    }

    public List<DespesaResponseDto> buscarPorCategoria(Despesa.Categoria categoria) {
        List<Despesa> despesas = despesaRepository.findByCategoria(categoria);
        return despesas.stream()
                .map(DespesaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<DespesaResponseDto> buscarPorUsuario(Long usuarioId) {
        return despesaRepository.findByUsuarioId(usuarioId).stream()
                .map(DespesaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DespesaResponseDto criarDespesa(DespesaCreateDto dto) {
        Despesa despesa = DespesaMapper.toEntity(dto);
        System.out.println("DTO recebido: " + dto);


        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId()));


        despesa.setUsuario(usuario);

        Despesa salva = despesaRepository.save(despesa);
        return DespesaMapper.toResponseDto(salva);


    }


}
