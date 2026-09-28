package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.talhao.TalhaoService;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto.DadosCadastroTalhao;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto.DadosDetalhamentoTalhao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/talhoes")
public class TalhaoController {

    @Autowired
    private TalhaoService talhaoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoTalhao>> listar() {
        return ResponseEntity.ok(talhaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTalhao> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(talhaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoTalhao> cadastrar(
            @RequestBody @Valid DadosCadastroTalhao dados,
            UriComponentsBuilder uriBuilder
    ) {
        var talhao = talhaoService.cadastrar(dados);
        var uri = uriBuilder.path("/talhoes/{id}").buildAndExpand(talhao.idTalhao()).toUri();
        return ResponseEntity.created(uri).body(talhao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTalhao> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroTalhao dados
    ) {
        return ResponseEntity.ok(talhaoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        talhaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
