package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.CulturaService;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto.DadosCadastroCultura;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto.DadosDetalhamentoCultura;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/culturas", "/culturas/"})
public class CulturaController {

    @Autowired
    private CulturaService culturaService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoCultura>> listar() {
        return ResponseEntity.ok(culturaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCultura> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(culturaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoCultura> cadastrar(
            @RequestBody @Valid DadosCadastroCultura dados,
            UriComponentsBuilder uriBuilder
    ) {
        var cultura = culturaService.cadastrar(dados);
        var uri = uriBuilder.path("/culturas/{id}").buildAndExpand(cultura.idCultura()).toUri();
        return ResponseEntity.created(uri).body(cultura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCultura> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroCultura dados
    ) {
        return ResponseEntity.ok(culturaService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        culturaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
