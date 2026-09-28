package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.insumo.InsumoService;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto.DadosCadastroInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto.DadosDetalhamentoInsumo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/insumos")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoInsumo>> listar() {
        return ResponseEntity.ok(insumoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInsumo> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(insumoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoInsumo> cadastrar(
            @RequestBody @Valid DadosCadastroInsumo dados,
            UriComponentsBuilder uriBuilder
    ) {
        var insumo = insumoService.cadastrar(dados);
        var uri = uriBuilder.path("/insumos/{id}").buildAndExpand(insumo.idInsumo()).toUri();
        return ResponseEntity.created(uri).body(insumo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInsumo> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroInsumo dados
    ) {
        return ResponseEntity.ok(insumoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        insumoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
