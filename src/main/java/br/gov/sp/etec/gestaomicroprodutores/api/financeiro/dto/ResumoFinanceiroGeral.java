package br.gov.sp.etec.gestaomicroprodutores.api.financeiro.dto;

import java.math.BigDecimal;

public record ResumoFinanceiroGeral(
        BigDecimal receitaTotalVendas,
        Long totalVendasRealizadas,
        BigDecimal custoTotalInsumos,
        BigDecimal custoTotalGastosProducao,
        BigDecimal custoTotalPerdasEstimadas,
        BigDecimal despesasTotais,
        BigDecimal saldoLiquido,
        BigDecimal margemLucroPercentual
) {
    public BigDecimal getCustoTotalGastos() {
        return custoTotalGastosProducao;
    }

    public BigDecimal getCustoTotalPerdas() {
        return custoTotalPerdasEstimadas;
    }
}
