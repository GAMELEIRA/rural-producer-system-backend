package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.safra.SafraService;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosAtualizacaoSafra;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosCadastroSafra;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosDetalhamentoSafra;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/safras", "/safras/"})
public class SafraController {

    @Autowired
    private SafraService safraService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoSafra>> listar() {
        return ResponseEntity.ok(safraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoSafra> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(safraService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DadosDetalhamentoSafra>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(safraService.listarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoSafra> cadastrar(
            @RequestBody @Valid DadosCadastroSafra dados,
            UriComponentsBuilder uriBuilder
    ) {
        var safra = safraService.cadastrar(dados);
        var uri = uriBuilder.path("/safras/{id}").buildAndExpand(safra.idSafra()).toUri();
        return ResponseEntity.created(uri).body(safra);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoSafra> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoSafra dados
    ) {
        return ResponseEntity.ok(safraService.atualizar(id, dados));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<DadosDetalhamentoSafra> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(safraService.finalizar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        safraService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
