package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoService;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.dto.DadosCadastroProduto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.dto.DadosDetalhamentoProduto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoProduto>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoProduto> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoProduto> cadastrar(
            @RequestBody @Valid DadosCadastroProduto dados,
            UriComponentsBuilder uriBuilder
    ) {
        var produto = produtoService.cadastrar(dados);
        var uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.idProduto()).toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoProduto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroProduto dados
    ) {
        return ResponseEntity.ok(produtoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
