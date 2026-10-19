package br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.insumo.Insumo;

import java.math.BigDecimal;

public record DadosDetalhamentoInsumo(
        Long idInsumo,
        String nome,
        String tipo,
        String unidadeMedida,
        BigDecimal quantidadeEstoque,
        BigDecimal precoUnitario,
        String observacoes,
        String imagemCodigo
) {
    public DadosDetalhamentoInsumo(Insumo insumo) {
        this(
                insumo.getIdInsumo(),
                insumo.getNome(),
                insumo.getTipo(),
                insumo.getUnidadeMedida(),
                insumo.getQuantidadeEstoque(),
                insumo.getPrecoUnitario(),
                insumo.getObservacoes(),
                insumo.getImagemCodigo()
        );
    }
}
