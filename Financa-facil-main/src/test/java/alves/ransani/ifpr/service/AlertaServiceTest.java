package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.Alerta;
import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.alerta.AlertaResponseDto;
import alves.ransani.ifpr.repository.AlertaRepository;
import alves.ransani.ifpr.repository.ParcelaRepository;
import alves.ransani.ifpr.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class AlertaServiceTest {

    @InjectMocks
    private AlertaService alertaService;

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ParcelaRepository parcelaRepository;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
    }

    @Test
    void deveCriarAlertaComSucesso() {
        String mensagem = "Você tem uma parcela vencendo!";
        Alerta alerta = new Alerta(null, mensagem, LocalDate.now(), false, usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alerta);

        Alerta resposta = alertaService.criarAlertaManual(1L, mensagem);

        assertNotNull(resposta);
        assertEquals(mensagem, resposta.getMensagem());
        assertEquals(usuario, resposta.getUsuario());
    }

    @Test
    void deveListarAlertasDoUsuario() {
        Alerta a1 = new Alerta(1L, "Parcela 1", LocalDate.now(), false, usuario);
        Alerta a2 = new Alerta(2L, "Parcela 2", LocalDate.now(), true, usuario);

        when(alertaRepository.findAll()).thenReturn(List.of(a1, a2));

        List<AlertaResponseDto> lista = alertaService.listarPorUsuario(1L);

        assertEquals(2, lista.size());
    }

    @Test
    void deveMarcarAlertaComoVisualizado() {
        Alerta alerta = new Alerta(1L, "Aviso", LocalDate.now(), false, usuario);

        when(alertaRepository.findById(1L)).thenReturn(Optional.of(alerta));

        Optional<AlertaResponseDto> resposta = alertaService.visualizar(1L);

        assertTrue(resposta.isPresent());
        assertTrue(alerta.isVisualizado());
        verify(alertaRepository).save(alerta);
    }
}
