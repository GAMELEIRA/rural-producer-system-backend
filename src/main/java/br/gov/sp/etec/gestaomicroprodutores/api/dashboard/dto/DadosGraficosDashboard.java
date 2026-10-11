package br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto;

import java.util.List;

public record DadosGraficosDashboard(
        List<ItemGraficoMes> vendasPorMes,
        List<ItemGraficoCategoria> despesasPorCategoria,
        List<ItemGraficoStatus> statusPlantacoes,
        List<ItemGraficoProduto> estoqueProdutos
) {
}
