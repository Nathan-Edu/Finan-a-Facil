package alves.ransani.ifpr.controller;


import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.usuario.LoginDto;
import alves.ransani.ifpr.dto.usuario.UsuarioRequestDto;
import alves.ransani.ifpr.dto.usuario.UsuarioResponseDto;
import alves.ransani.ifpr.mapper.UsuarioMapper;
import alves.ransani.ifpr.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDto> login(@RequestBody LoginDto loginDto) {
        return usuarioService.autenticar(loginDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody UsuarioRequestDto dto) {
        try {
            if (!usuarioService.emailDisponivel(dto.getEmail())) {
                return ResponseEntity.badRequest().body(null);
            }

            Usuario usuario = UsuarioMapper.toEntity(dto);
            UsuarioResponseDto response = usuarioService.cadastrar(usuario);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

}
