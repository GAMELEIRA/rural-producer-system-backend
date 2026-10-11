package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueService;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosDetalhamentoMovimentacao;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosEntradaEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosSaidaEstoque;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/estoque", "/estoque/"})
public class EstoqueController {

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoMovimentacao>> listar() {
        return ResponseEntity.ok(movimentacaoEstoqueService.listar());
    }

    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<List<DadosDetalhamentoMovimentacao>> listarPorProduto(@PathVariable Long idProduto) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorProduto(idProduto));
    }

    @PostMapping("/entrada")
    public ResponseEntity<DadosDetalhamentoMovimentacao> registrarEntrada(@RequestBody @Valid DadosEntradaEstoque dados) {
        return ResponseEntity.ok(movimentacaoEstoqueService.registrarEntrada(dados));
    }

    @PostMapping("/saida")
    public ResponseEntity<DadosDetalhamentoMovimentacao> registrarSaida(@RequestBody @Valid DadosSaidaEstoque dados) {
        return ResponseEntity.ok(movimentacaoEstoqueService.registrarSaida(dados));
    }
}
