package alves.ransani.ifpr.controller;
import alves.ransani.ifpr.dao.Despesa;
import alves.ransani.ifpr.dto.despesa.DespesaCreateDto;
import alves.ransani.ifpr.dto.despesa.DespesaResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import alves.ransani.ifpr.service.DespesaService;

import java.util.List;


@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<DespesaResponseDto> criarDespesa(@RequestBody DespesaCreateDto dto) {
        DespesaResponseDto resposta = despesaService.criarDespesa(dto);
        return ResponseEntity.ok(resposta);

    }

    @GetMapping
    public ResponseEntity<List<DespesaResponseDto>> listarTodas() {
        return ResponseEntity.ok(despesaService.listarTodas());

    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DespesaResponseDto>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<DespesaResponseDto> despesas = despesaService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(despesas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponseDto> atualizar(@PathVariable Long id, @RequestBody DespesaCreateDto dto) {
        return despesaService.atualizarDespesa(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = despesaService.deletarDespesa(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<DespesaResponseDto>> buscarPorCategoria(@PathVariable String categoria) {
        try {
            Despesa.Categoria categoriaEnum = Despesa.Categoria.valueOf(categoria.toUpperCase());
            List<DespesaResponseDto> despesas = despesaService.buscarPorCategoria(categoriaEnum);
            return ResponseEntity.ok(despesas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
