package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueService;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoInsumoService;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosEntradaInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosDetalhamentoMovimentacaoInsumo;
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

    @Autowired
    private MovimentacaoInsumoService movimentacaoInsumoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoMovimentacao>> listar() {
        return ResponseEntity.ok(movimentacaoEstoqueService.listar());
    }

    @GetMapping("/insumos")
    public ResponseEntity<List<DadosDetalhamentoMovimentacaoInsumo>> listarInsumos() {
        return ResponseEntity.ok(movimentacaoInsumoService.listar());
    }

    @PostMapping("/insumos/entrada")
    public ResponseEntity<DadosDetalhamentoMovimentacaoInsumo> entradaInsumo(@RequestBody @Valid DadosEntradaInsumo dados) {
        return ResponseEntity.ok(movimentacaoInsumoService.registrarEntrada(dados));
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
