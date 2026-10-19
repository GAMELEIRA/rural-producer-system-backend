package br.gov.sp.etec.gestaomicroprodutores.api.produto.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;

import java.math.BigDecimal;

public record DadosDetalhamentoProduto(
        Long idProduto,
        String nome,
        String unidadeMedida,
        BigDecimal precoVenda,
        BigDecimal quantidadeEstoque,
        String observacoes,
        Long idCultura,
        String imagemCodigo,
        String imagemPersonalizada
) {
    public DadosDetalhamentoProduto(Produto produto) {
        this(
                produto.getIdProduto(),
                produto.getNome(),
                produto.getUnidadeMedida(),
                produto.getPrecoVenda(),
                produto.getQuantidadeEstoque(),
                produto.getObservacoes(),
                produto.getCultura() != null ? produto.getCultura().getIdCultura() : null,
                produto.getImagemCodigo(),
                produto.getImagemPersonalizada()
        );
    }
}
