package alves.ransani.ifpr.controller;
import alves.ransani.ifpr.dto.receita.ReceitaCreateDto;
import alves.ransani.ifpr.dto.receita.ReceitaResponseDto;
import alves.ransani.ifpr.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<ReceitaResponseDto> criar(@RequestBody ReceitaCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.criarReceita(dto));
    }


    @GetMapping
    public ResponseEntity<List<ReceitaResponseDto>> listarTodas() {
        return ResponseEntity.ok(receitaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDto> buscarPorId(@PathVariable Long id) {
        return receitaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDto> atualizar(@PathVariable Long id, @RequestBody ReceitaCreateDto dto) {
        return receitaService.atualizarReceita(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = receitaService.deletarReceita(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReceitaResponseDto>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ReceitaResponseDto> receitas = receitaService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(receitas);
    }

}
