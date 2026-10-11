package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto;

import java.math.BigDecimal;

public record ResumoCustoInsumosPlantacao(
        Long idPlantacao,
        Long totalAplicacoes,
        BigDecimal custoTotalInsumos
) {
}
