package br.gov.sp.etec.gestaomicroprodutores.api.perda.dto;

import java.math.BigDecimal;

public record ResumoPerdasPlantacao(
        Long idPlantacao,
        Long totalOcorrencias,
        BigDecimal quantidadeTotalPerdida,
        BigDecimal valorTotalEstimado
) {
}
