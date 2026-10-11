package br.gov.sp.etec.gestaomicroprodutores.api.venda.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.venda.ItemVenda;

import java.math.BigDecimal;

public record DadosDetalhamentoItemVenda(
        Long idItemVenda,
        Long idProduto,
        String nomeProduto,
        String unidadeMedida,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public DadosDetalhamentoItemVenda(ItemVenda item) {
        this(
                item.getIdItemVenda(),
                item.getProduto().getIdProduto(),
                item.getProduto().getNome(),
                item.getProduto().getUnidadeMedida(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}
