package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.dashboard.DashboardService;
import br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto.DadosGraficosDashboard;
import br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto.DadosResumoDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/dashboard", "/dashboard/"})
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/resumo")
    public ResponseEntity<DadosResumoDashboard> obterResumo() {
        return ResponseEntity.ok(dashboardService.obterResumo());
    }

    @GetMapping("/graficos")
    public ResponseEntity<DadosGraficosDashboard> obterGraficos() {
        return ResponseEntity.ok(dashboardService.obterGraficos());
    }
}
