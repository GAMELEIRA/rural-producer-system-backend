package br.gov.sp.etec.gestaomicroprodutores.api.financeiro.dto;

import java.math.BigDecimal;

public record ResumoFinanceiroPlantacao(
        Long idPlantacao,
        String cultura,
        String talhao,
        String status,
        BigDecimal custoInsumos,
        BigDecimal gastosProducao,
        BigDecimal perdasEstimadas,
        BigDecimal custoTotalProducao
) {
}
