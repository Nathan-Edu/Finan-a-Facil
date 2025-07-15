package alves.ransani.ifpr.service;
import alves.ransani.ifpr.dao.Receita;
import alves.ransani.ifpr.dto.receita.ReceitaCreateDto;
import alves.ransani.ifpr.dto.receita.ReceitaResponseDto;
import alves.ransani.ifpr.mapper.ReceitaMapper;
import alves.ransani.ifpr.repository.ReceitaRepository;
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

public class ReceitaServiceTest {

    @InjectMocks
    private ReceitaService receitaService;

    @Mock
    private ReceitaRepository receitaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarReceitaComSucesso() {
        ReceitaCreateDto dto = new ReceitaCreateDto();
        dto.setDescricao("Salário");
        dto.setValor(new BigDecimal("3500.00"));
        dto.setData(LocalDate.of(2025, 5, 10));

        Receita receita = ReceitaMapper.toEntity(dto);
        receita.setId(1L);

        when(receitaRepository.save(any(Receita.class))).thenReturn(receita);

        ReceitaResponseDto response = receitaService.criarReceita(dto);

        assertNotNull(response);
        assertEquals("Salário", response.getDescricao());
        assertEquals(new BigDecimal("3500.00"), response.getValor());
    }

    @Test
    void deveBuscarReceitaPorId() {
        Receita receita = new Receita();
        receita.setId(1L);
        receita.setDescricao("Freelancer");
        receita.setValor(new BigDecimal("900.00"));
        receita.setData(LocalDate.of(2025, 5, 5));

        when(receitaRepository.findById(1L)).thenReturn(Optional.of(receita));

        Optional<ReceitaResponseDto> resultado = receitaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Freelancer", resultado.get().getDescricao());
    }
}
