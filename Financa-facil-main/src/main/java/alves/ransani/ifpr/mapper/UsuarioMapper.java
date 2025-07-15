package alves.ransani.ifpr.mapper;

import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.usuario.UsuarioRequestDto;
import alves.ransani.ifpr.dto.usuario.UsuarioResponseDto;

public class UsuarioMapper {

    public static UsuarioResponseDto toDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        return dto;
    }

    public static Usuario toEntity(String nome, String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
    }

    public static Usuario toEntity(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        return usuario;
    }
}
