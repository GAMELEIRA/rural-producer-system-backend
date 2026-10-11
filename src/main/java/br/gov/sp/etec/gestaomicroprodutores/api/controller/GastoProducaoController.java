package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducaoService;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosAtualizacaoGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosCadastroGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosDetalhamentoGasto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/gastos", "/gastos/"})
public class GastoProducaoController {

    @Autowired
    private GastoProducaoService gastoProducaoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoGasto>> listar() {
        return ResponseEntity.ok(gastoProducaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoGasto> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(gastoProducaoService.buscarPorId(id));
    }

    @GetMapping("/plantacao/{idPlantacao}")
    public ResponseEntity<List<DadosDetalhamentoGasto>> listarPorPlantacao(@PathVariable Long idPlantacao) {
        return ResponseEntity.ok(gastoProducaoService.listarPorPlantacao(idPlantacao));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoGasto> cadastrar(
            @RequestBody @Valid DadosCadastroGasto dados,
            UriComponentsBuilder uriBuilder
    ) {
        var gasto = gastoProducaoService.cadastrar(dados);
        var uri = uriBuilder.path("/gastos/{id}").buildAndExpand(gasto.idGasto()).toUri();
        return ResponseEntity.created(uri).body(gasto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoGasto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoGasto dados
    ) {
        return ResponseEntity.ok(gastoProducaoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        gastoProducaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
