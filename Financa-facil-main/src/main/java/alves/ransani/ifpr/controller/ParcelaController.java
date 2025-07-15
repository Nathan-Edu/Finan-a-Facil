package alves.ransani.ifpr.controller;

import alves.ransani.ifpr.dto.parcela.ParcelaResponseDto;
import alves.ransani.ifpr.service.ParcelaService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parcelas")
public class ParcelaController {

    @Autowired
    private ParcelaService parcelaService;

    @GetMapping
    public ResponseEntity<List<ParcelaResponseDto>> listarTodas() {
        return ResponseEntity.ok(parcelaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcelaResponseDto> buscarPorId(@PathVariable Long id) {
        return parcelaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Void> marcarComoPaga(@PathVariable Long id) {
        boolean sucesso = parcelaService.marcarComoPaga(id);
        return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<ParcelaResponseDto>> listarPorConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(parcelaService.listarPorContaParcelada(contaId));
    }

}
