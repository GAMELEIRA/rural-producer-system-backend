package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoService;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.DadosAtualizacaoAplicacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.DadosCadastroAplicacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.DadosDetalhamentoAplicacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.ResumoCustoInsumosPlantacao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/aplicacoes-insumo", "/aplicacoes-insumo/"})
public class AplicacaoInsumoController {

    @Autowired
    private AplicacaoInsumoService aplicacaoInsumoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoAplicacaoInsumo>> listar() {
        return ResponseEntity.ok(aplicacaoInsumoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAplicacaoInsumo> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(aplicacaoInsumoService.buscarPorId(id));
    }

    @GetMapping("/plantacao/{idPlantacao}")
    public ResponseEntity<List<DadosDetalhamentoAplicacaoInsumo>> listarPorPlantacao(@PathVariable Long idPlantacao) {
        return ResponseEntity.ok(aplicacaoInsumoService.listarPorPlantacao(idPlantacao));
    }

    @GetMapping("/plantacao/{idPlantacao}/resumo-custo")
    public ResponseEntity<ResumoCustoInsumosPlantacao> obterResumoCusto(@PathVariable Long idPlantacao) {
        return ResponseEntity.ok(aplicacaoInsumoService.obterResumoCustoPorPlantacao(idPlantacao));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAplicacaoInsumo> cadastrar(
            @RequestBody @Valid DadosCadastroAplicacaoInsumo dados,
            UriComponentsBuilder uriBuilder
    ) {
        var aplicacao = aplicacaoInsumoService.cadastrar(dados);
        var uri = uriBuilder.path("/aplicacoes-insumo/{id}").buildAndExpand(aplicacao.idAplicacao()).toUri();
        return ResponseEntity.created(uri).body(aplicacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAplicacaoInsumo> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoAplicacaoInsumo dados
    ) {
        return ResponseEntity.ok(aplicacaoInsumoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        aplicacaoInsumoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
