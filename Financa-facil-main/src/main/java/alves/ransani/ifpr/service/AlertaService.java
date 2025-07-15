package alves.ransani.ifpr.service;

import alves.ransani.ifpr.dao.Alerta;
import alves.ransani.ifpr.dao.ContaParcelada;
import alves.ransani.ifpr.dao.Parcela;
import alves.ransani.ifpr.dao.Usuario;
import alves.ransani.ifpr.dto.alerta.AlertaResponseDto;
import alves.ransani.ifpr.mapper.AlertaMapper;
import alves.ransani.ifpr.repository.AlertaRepository;
import alves.ransani.ifpr.repository.ParcelaRepository;
import alves.ransani.ifpr.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private ParcelaRepository parcelaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Alerta criarAlertaManual(Long usuarioId, String mensagem) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Alerta alerta = new Alerta();
        alerta.setMensagem(mensagem);
        alerta.setDataGeracao(LocalDate.now());
        alerta.setVisualizado(false);
        alerta.setUsuario(usuario);

        return alertaRepository.save(alerta);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void gerarAlertasDeParcelas() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(3);

        List<Parcela> parcelas = parcelaRepository.findByDataVencimentoBetween(hoje, dataLimite);

        for (Parcela parcela : parcelas) {
            if (!parcela.isPaga() && parcela.getContaParcelada() != null) {
                Long usuarioId = parcela.getContaParcelada().getUsuario().getId();
                String mensagem = "Parcela nº " + parcela.getNumero() + " vence em breve: " + parcela.getDataVencimento();

                boolean alertaExistente = alertaRepository.findByUsuarioId(usuarioId).stream()
                        .anyMatch(alerta -> alerta.getMensagem().equals(mensagem));

                if (!alertaExistente) {
                    criarAlertaManual(usuarioId, mensagem);
                }
            }
        }
    }

    public AlertaResponseDto criar(Long usuarioId, String mensagem, LocalDate dataAlerta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Alerta alerta = new Alerta();
        alerta.setMensagem(mensagem);
        alerta.setDataGeracao(dataAlerta != null ? dataAlerta : LocalDate.now());
        alerta.setVisualizado(false);
        alerta.setUsuario(usuario);

        return AlertaMapper.toResponseDto(alertaRepository.save(alerta));
    }


    public List<AlertaResponseDto> listarPorUsuario(Long usuarioId) {
        return alertaRepository.findByUsuarioId(usuarioId).stream()
                .map(AlertaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<AlertaResponseDto> visualizar(Long id) {
        return alertaRepository.findById(id).map(alerta -> {
            alerta.setVisualizado(true);
            alertaRepository.save(alerta);
            return AlertaMapper.toResponseDto(alerta);
        });
    }

    public boolean deletar(Long id) {
        return alertaRepository.findById(id).map(alerta -> {
            alertaRepository.delete(alerta);
            return true;
        }).orElse(false);
    }

    public void verificarEAlertarContaParcelada(ContaParcelada contaParcelada) {
        if (contaParcelada == null || contaParcelada.getUsuario() == null) return;

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(3);

        for (Parcela parcela : contaParcelada.getParcelas()) {
            if (!parcela.isPaga() && parcela.getNumero() == 1 &&
                    !parcela.getDataVencimento().isBefore(hoje) &&
                    !parcela.getDataVencimento().isAfter(limite)) {

                String mensagem = "Primeira parcela da conta '" + contaParcelada.getDescricao() +
                        "' vence em " + parcela.getDataVencimento();

                boolean alertaExistente = alertaRepository.findByUsuarioId(contaParcelada.getUsuario().getId())
                        .stream()
                        .anyMatch(alerta -> alerta.getMensagem().equals(mensagem));

                if (!alertaExistente) {
                    criarAlertaManual(contaParcelada.getUsuario().getId(), mensagem);
                }
                break;
            }
        }
    }
}
