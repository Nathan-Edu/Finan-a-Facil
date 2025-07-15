package alves.ransani.ifpr.controller;

import alves.ransani.ifpr.dto.alerta.AlertaResponseDto;
import alves.ransani.ifpr.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import alves.ransani.ifpr.dto.alerta.NovoAlertaDto;

import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    public ResponseEntity<AlertaResponseDto> criarAlerta(@RequestBody NovoAlertaDto dto) {
        return ResponseEntity.ok(alertaService.criar(dto.getUsuarioId(), dto.getMensagem(), dto.getDataAlerta()));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AlertaResponseDto>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(alertaService.listarPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/visualizar")
    public ResponseEntity<AlertaResponseDto> marcarComoVisualizado(@PathVariable Long id) {
        return alertaService.visualizar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = alertaService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
