package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.perda.PerdaService;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosAtualizacaoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosCadastroPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosDetalhamentoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.ResumoPerdasPlantacao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/perdas", "/perdas/"})
public class PerdaController {

    @Autowired
    private PerdaService perdaService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoPerda>> listar() {
        return ResponseEntity.ok(perdaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPerda> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(perdaService.buscarPorId(id));
    }

    @GetMapping("/plantacao/{idPlantacao}")
    public ResponseEntity<List<DadosDetalhamentoPerda>> listarPorPlantacao(@PathVariable Long idPlantacao) {
        return ResponseEntity.ok(perdaService.listarPorPlantacao(idPlantacao));
    }

    @GetMapping("/plantacao/{idPlantacao}/resumo")
    public ResponseEntity<ResumoPerdasPlantacao> obterResumoPorPlantacao(@PathVariable Long idPlantacao) {
        return ResponseEntity.ok(perdaService.obterResumoPorPlantacao(idPlantacao));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPerda> cadastrar(
            @RequestBody @Valid DadosCadastroPerda dados,
            UriComponentsBuilder uriBuilder
    ) {
        var perda = perdaService.cadastrar(dados);
        var uri = uriBuilder.path("/perdas/{id}").buildAndExpand(perda.idPerda()).toUri();
        return ResponseEntity.created(uri).body(perda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPerda> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoPerda dados
    ) {
        return ResponseEntity.ok(perdaService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        perdaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
