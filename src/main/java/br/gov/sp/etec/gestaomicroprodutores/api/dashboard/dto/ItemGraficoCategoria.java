package br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto;

import java.math.BigDecimal;

public record ItemGraficoCategoria(
        String categoria,
        BigDecimal total
) {
}
