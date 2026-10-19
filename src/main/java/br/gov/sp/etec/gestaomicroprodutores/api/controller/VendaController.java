package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.venda.VendaService;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.dto.DadosCadastroVenda;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.dto.DadosDetalhamentoVenda;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/vendas", "/vendas/"})
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoVenda>> listar() {
        return ResponseEntity.ok(vendaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoVenda> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DadosDetalhamentoVenda>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(vendaService.listarPorStatus(status));
    }

    @GetMapping("/cliente/{cliente}")
    public ResponseEntity<List<DadosDetalhamentoVenda>> listarPorCliente(@PathVariable String cliente) {
        return ResponseEntity.ok(vendaService.listarPorCliente(cliente));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoVenda> cadastrar(
            @RequestBody @Valid DadosCadastroVenda dados,
            UriComponentsBuilder uriBuilder
    ) {
        var venda = vendaService.cadastrar(dados);
        var uri = uriBuilder.path("/vendas/{id}").buildAndExpand(venda.idVenda()).toUri();
        return ResponseEntity.created(uri).body(venda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoVenda> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroVenda dados) {
        return ResponseEntity.ok(vendaService.atualizar(id, dados));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<DadosDetalhamentoVenda> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.cancelar(id));
    }
}
