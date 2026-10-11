package br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto;

import java.math.BigDecimal;

public record ItemGraficoProduto(
        Long idProduto,
        String nome,
        String unidadeMedida,
        BigDecimal quantidadeEstoque,
        BigDecimal precoVenda,
        BigDecimal valorTotal
) {
}
