package br.gov.sp.etec.gestaomicroprodutores.api.perda.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.perda.Perda;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoPerda(
        Long idPerda,
        Long idPlantacao,
        String nomeTalhao,
        String nomeCultura,
        LocalDate dataPerda,
        BigDecimal quantidade,
        String motivo,
        BigDecimal valorEstimado,
        String observacoes,
        String etapa
) {
    public DadosDetalhamentoPerda(Perda perda) {
        this(
                perda.getIdPerda(),
                perda.getPlantacao().getIdPlantacao(),
                perda.getPlantacao().getTalhao().getNome(),
                perda.getPlantacao().getCultura().getNome(),
                perda.getDataPerda(),
                perda.getQuantidade(),
                perda.getMotivo(),
                perda.getValorEstimado(),
                perda.getObservacoes(),
                perda.getEtapa()
        );
    }
}
