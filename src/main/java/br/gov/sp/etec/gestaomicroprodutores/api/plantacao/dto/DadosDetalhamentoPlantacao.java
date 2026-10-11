package br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoPlantacao(
        Long idPlantacao,
        Long idTalhao,
        String nomeTalhao,
        Long idCultura,
        String nomeCultura,
        LocalDate dataPlantio,
        LocalDate dataColheita,
        BigDecimal quantidadePlantada,
        BigDecimal quantidadeColhida,
        String status,
        String observacoes
) {
    public DadosDetalhamentoPlantacao(Plantacao plantacao) {
        this(
                plantacao.getIdPlantacao(),
                plantacao.getTalhao().getIdTalhao(),
                plantacao.getTalhao().getNome(),
                plantacao.getCultura().getIdCultura(),
                plantacao.getCultura().getNome(),
                plantacao.getDataPlantio(),
                plantacao.getDataColheita(),
                plantacao.getQuantidadePlantada(),
                plantacao.getQuantidadeColhida(),
                plantacao.getStatus(),
                plantacao.getObservacoes()
        );
    }
}
