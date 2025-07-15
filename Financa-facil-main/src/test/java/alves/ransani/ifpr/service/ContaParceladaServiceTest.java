package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.ContaParcelada;
import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.conta.ContaParceladaCreateDto;
import alves.ransani.ifpr.dto.conta.ContaParceladaResponseDto;
import alves.ransani.ifpr.mapper.ContaParceladaMapper;
import alves.ransani.ifpr.repository.ContaParceladaRepository;
import alves.ransani.ifpr.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class ContaParceladaServiceTest {

    @InjectMocks
    private ContaParceladaService contaService;

    @Mock
    private ContaParceladaRepository contaParceladaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarContaParceladaComSucesso() {
        ContaParceladaCreateDto dto = new ContaParceladaCreateDto();
        dto.setDescricao("Notebook");
        dto.setValorTotal(new BigDecimal("3000.00"));
        dto.setQuantidadeParcelas(6);
        dto.setDataInicio(LocalDate.of(2025, 5, 1));
        dto.setUsuarioId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Ana");

        ContaParcelada conta = ContaParceladaMapper.toEntity(dto);
        conta.setId(1L);
        conta.setUsuario(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaParceladaRepository.save(any(ContaParcelada.class))).thenReturn(conta);

        ContaParceladaResponseDto response = contaService.criarContaParcelada(dto);

        assertNotNull(response);
        assertEquals("Notebook", response.getDescricao());
        assertEquals(6, response.getParcelas());
    }

    @Test
    void deveBuscarContaPorId() {
        ContaParcelada conta = new ContaParcelada();
        conta.setId(2L);
        conta.setDescricao("Celular");
        conta.setValorTotal(new BigDecimal("2400.00"));
        conta.setQuantidadeParcelas(12);
        conta.setDataInicio(LocalDate.of(2025, 4, 15));

        when(contaParceladaRepository.findById(2L)).thenReturn(Optional.of(conta));

        Optional<ContaParceladaResponseDto> resultado = contaService.buscarPorId(2L);

        assertTrue(resultado.isPresent());
        assertEquals("Celular", resultado.get().getDescricao());
    }
}
