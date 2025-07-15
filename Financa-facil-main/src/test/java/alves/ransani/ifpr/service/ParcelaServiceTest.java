package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.ContaParcelada;
import alves.ransani.ifpr.dao.Parcela;
import alves.ransani.ifpr.dto.parcela.ParcelaResponseDto;
import alves.ransani.ifpr.repository.ParcelaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class ParcelaServiceTest {

    @InjectMocks
    private ParcelaService parcelaService;

    @Mock
    private ParcelaRepository parcelaRepository;

    private ContaParcelada conta;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        conta = new ContaParcelada();
        conta.setId(1L);
    }

    @Test
    void deveListarTodasAsParcelas() {
        Parcela p1 = new Parcela(1L, 1, new BigDecimal("100.00"), LocalDate.now(), false, conta);
        Parcela p2 = new Parcela(2L, 2, new BigDecimal("100.00"), LocalDate.now().plusMonths(1), false, conta);

        when(parcelaRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ParcelaResponseDto> resultado = parcelaService.listarTodas();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveBuscarParcelaPorId() {
        Parcela parcela = new Parcela(1L, 1, new BigDecimal("200.00"), LocalDate.now(), false, conta);

        when(parcelaRepository.findById(1L)).thenReturn(Optional.of(parcela));

        Optional<ParcelaResponseDto> resultado = parcelaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getNumero());
    }

    @Test
    void deveMarcarParcelaComoPaga() {
        Parcela parcela = new Parcela(1L, 1, new BigDecimal("100.00"), LocalDate.now(), false, conta);

        when(parcelaRepository.findById(1L)).thenReturn(Optional.of(parcela));

        boolean sucesso = parcelaService.marcarComoPaga(1L);

        assertTrue(sucesso);
        assertTrue(parcela.isPaga());
        verify(parcelaRepository).save(parcela);
    }

    @Test
    void deveListarParcelasPorConta() {
        Parcela p1 = new Parcela(1L, 1, new BigDecimal("100.00"), LocalDate.now(), false, conta);
        Parcela p2 = new Parcela(2L, 2, new BigDecimal("100.00"), LocalDate.now().plusMonths(1), false, conta);

        when(parcelaRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ParcelaResponseDto> resultado = parcelaService.listarPorContaParcelada(1L);

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getNumero());
    }
}
