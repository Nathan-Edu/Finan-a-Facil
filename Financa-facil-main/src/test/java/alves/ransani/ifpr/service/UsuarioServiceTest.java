package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.usuario.LoginDto;
import alves.ransani.ifpr.dto.usuario.UsuarioResponseDto;
import alves.ransani.ifpr.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("1234");
    }

    @Test
    void deveAutenticarUsuarioComCredenciaisValidas() {
        LoginDto login = new LoginDto();
        login.setEmail("maria@email.com");
        login.setSenha("1234");

        when(usuarioRepository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDto> resultado = usuarioService.autenticar(login);

        assertTrue(resultado.isPresent());
        assertEquals("Maria", resultado.get().getNome());
    }

    @Test
    void naoDeveAutenticarUsuarioComSenhaIncorreta() {
        LoginDto login = new LoginDto();
        login.setEmail("maria@email.com");
        login.setSenha("senhaErrada");

        when(usuarioRepository.findByEmail("maria@email.com"))
                .thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDto> resultado = usuarioService.autenticar(login);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveConfirmarEmailDisponivel() {
        when(usuarioRepository.findByEmail("livre@email.com"))
                .thenReturn(Optional.empty());

        boolean disponivel = usuarioService.emailDisponivel("livre@email.com");

        assertTrue(disponivel);
    }

    @Test
    void deveCadastrarNovoUsuario() {
        Usuario novo = new Usuario(null, "João", "joao@email.com", "4321");

        Usuario salvo = new Usuario(2L, "João", "joao@email.com", "4321");

        when(usuarioRepository.save(novo)).thenReturn(salvo);

        UsuarioResponseDto response = usuarioService.cadastrar(novo);

        assertNotNull(response);
        assertEquals("João", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
    }
}
