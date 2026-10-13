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
        Long idSafra,
        String nomeSafra,
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
                plantacao.getSafra() != null ? plantacao.getSafra().getIdSafra() : null,
                plantacao.getSafra() != null ? plantacao.getSafra().getNome() : null,
                plantacao.getDataPlantio(),
                plantacao.getDataColheita(),
                plantacao.getQuantidadePlantada(),
                plantacao.getQuantidadeColhida(),
                plantacao.getStatus(),
                plantacao.getObservacoes()
        );
    }
}
