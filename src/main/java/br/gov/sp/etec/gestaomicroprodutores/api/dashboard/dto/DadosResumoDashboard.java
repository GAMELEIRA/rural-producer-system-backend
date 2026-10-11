package br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto;

import java.math.BigDecimal;

public record DadosResumoDashboard(
        Long totalTalhoes,
        Long totalCulturas,
        Long totalPlantacoesAtivas,
        Long totalSafrasEmAndamento,
        Long totalProdutosCadastrados,
        BigDecimal valorTotalEstoqueProdutos,
        BigDecimal valorTotalEstoqueInsumos,
        BigDecimal receitaTotalVendas,
        Long totalVendasRealizadas,
        BigDecimal custoTotalInsumos,
        BigDecimal custoTotalGastos,
        BigDecimal custoTotalPerdas,
        BigDecimal despesasTotais,
        BigDecimal saldoLiquido,
        BigDecimal margemLucroPercentual
) {
}
