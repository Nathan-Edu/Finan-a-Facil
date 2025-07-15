package alves.ransani.ifpr.controller;


import alves.ransani.ifpr.dto.conta.ContaParceladaCreateDto;
import alves.ransani.ifpr.dto.conta.ContaParceladaResponseDto;
import alves.ransani.ifpr.service.ContaParceladaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas-parceladas")
public class ContaParceladaController {

    @Autowired
    private ContaParceladaService contaParceladaService;

    @PostMapping
    public ResponseEntity<ContaParceladaResponseDto> criar(@RequestBody ContaParceladaCreateDto dto) {
        ContaParceladaResponseDto resposta = contaParceladaService.criarContaParcelada(dto);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<ContaParceladaResponseDto>> listarTodas() {
        return ResponseEntity.ok(contaParceladaService.listarTodas());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaParceladaResponseDto>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ContaParceladaResponseDto> contas = contaParceladaService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(contas);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ContaParceladaResponseDto> atualizar(@PathVariable Long id,
                                                               @RequestBody ContaParceladaCreateDto dto) {
        return contaParceladaService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = contaParceladaService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
