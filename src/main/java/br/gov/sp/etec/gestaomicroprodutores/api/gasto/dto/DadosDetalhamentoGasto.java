package br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoGasto(
        Long idGasto,
        Long idPlantacao,
        String nomeCultura,
        String nomeTalhao,
        String descricao,
        BigDecimal valor,
        LocalDate dataGasto,
        String categoria,
        String observacoes
) {
    public DadosDetalhamentoGasto(GastoProducao gasto) {
        this(
                gasto.getIdGasto(),
                gasto.getPlantacao().getIdPlantacao(),
                gasto.getPlantacao().getCultura().getNome(),
                gasto.getPlantacao().getTalhao().getNome(),
                gasto.getDescricao(),
                gasto.getValor(),
                gasto.getDataGasto(),
                gasto.getCategoria(),
                gasto.getObservacoes()
        );
    }
}
