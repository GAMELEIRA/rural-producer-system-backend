package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoService;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosAtualizacaoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosCadastroPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosDetalhamentoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosFinalizarColheita;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/plantacoes", "/plantacoes/"})
public class PlantacaoController {

    @Autowired
    private PlantacaoService plantacaoService;

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoPlantacao>> listar() {
        return ResponseEntity.ok(plantacaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPlantacao> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(plantacaoService.buscarPorId(id));
    }

    @GetMapping("/talhao/{idTalhao}")
    public ResponseEntity<List<DadosDetalhamentoPlantacao>> listarPorTalhao(@PathVariable Long idTalhao) {
        return ResponseEntity.ok(plantacaoService.listarPorTalhao(idTalhao));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DadosDetalhamentoPlantacao>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(plantacaoService.listarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPlantacao> cadastrar(
            @RequestBody @Valid DadosCadastroPlantacao dados,
            UriComponentsBuilder uriBuilder
    ) {
        var plantacao = plantacaoService.cadastrar(dados);
        var uri = uriBuilder.path("/plantacoes/{id}").buildAndExpand(plantacao.idPlantacao()).toUri();
        return ResponseEntity.created(uri).body(plantacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPlantacao> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoPlantacao dados
    ) {
        return ResponseEntity.ok(plantacaoService.atualizar(id, dados));
    }

    @PatchMapping("/{id}/finalizar-colheita")
    public ResponseEntity<DadosDetalhamentoPlantacao> finalizarColheita(
            @PathVariable Long id,
            @RequestBody @Valid DadosFinalizarColheita dados
    ) {
        return ResponseEntity.ok(plantacaoService.finalizarColheita(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        plantacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
