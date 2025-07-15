package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.usuario.LoginDto;
import alves.ransani.ifpr.dto.usuario.UsuarioResponseDto;
import alves.ransani.ifpr.mapper.UsuarioMapper;
import alves.ransani.ifpr.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<UsuarioResponseDto> autenticar(LoginDto loginDto) {
        return usuarioRepository.findByEmail(loginDto.getEmail())
                .filter(usuario -> usuario.getSenha().equals(loginDto.getSenha()))
                .map(UsuarioMapper::toDto);
    }

    public boolean emailDisponivel(String email) {
        return usuarioRepository.findByEmail(email).isEmpty();
    }

    public UsuarioResponseDto cadastrar(Usuario usuario) {
        if (!emailDisponivel(usuario.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        try {
            Usuario salvo = usuarioRepository.save(usuario);
            return UsuarioMapper.toDto(salvo);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro ao cadastrar usuário. Verifique os dados.");
        }
    }

    public UsuarioResponseDto salvar(String nome, String email, String senha) {
        Usuario usuario = UsuarioMapper.toEntity(nome, email, senha);
        return cadastrar(usuario);
    }
}
