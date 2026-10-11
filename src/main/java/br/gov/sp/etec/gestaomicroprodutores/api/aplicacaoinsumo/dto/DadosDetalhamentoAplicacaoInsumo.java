package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoAplicacaoInsumo(
        Long idAplicacao,
        Long idPlantacao,
        String nomeTalhao,
        String nomeCultura,
        Long idInsumo,
        String nomeInsumo,
        String unidadeMedidaInsumo,
        BigDecimal precoUnitarioInsumo,
        LocalDate dataAplicacao,
        BigDecimal quantidade,
        BigDecimal custoTotal,
        String observacoes
) {
    public DadosDetalhamentoAplicacaoInsumo(AplicacaoInsumo aplicacao) {
        this(
                aplicacao.getIdAplicacao(),
                aplicacao.getPlantacao().getIdPlantacao(),
                aplicacao.getPlantacao().getTalhao().getNome(),
                aplicacao.getPlantacao().getCultura().getNome(),
                aplicacao.getInsumo().getIdInsumo(),
                aplicacao.getInsumo().getNome(),
                aplicacao.getInsumo().getUnidadeMedida(),
                aplicacao.getInsumo().getPrecoUnitario(),
                aplicacao.getDataAplicacao(),
                aplicacao.getQuantidade(),
                aplicacao.getCustoTotal(),
                aplicacao.getObservacoes()
        );
    }
}
