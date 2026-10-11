package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.financeiro.FinanceiroService;
import br.gov.sp.etec.gestaomicroprodutores.api.financeiro.dto.ResumoFinanceiroGeral;
import br.gov.sp.etec.gestaomicroprodutores.api.financeiro.dto.ResumoFinanceiroPlantacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/financeiro", "/financeiro/"})
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @GetMapping("/resumo")
    public ResponseEntity<ResumoFinanceiroGeral> obterResumoGeral() {
        return ResponseEntity.ok(financeiroService.obterResumoGeral());
    }

    @GetMapping("/plantacao/{idPlantacao}")
    public ResponseEntity<ResumoFinanceiroPlantacao> obterResumoPlantacao(@PathVariable Long idPlantacao) {
        return ResponseEntity.ok(financeiroService.obterResumoPlantacao(idPlantacao));
    }
}
