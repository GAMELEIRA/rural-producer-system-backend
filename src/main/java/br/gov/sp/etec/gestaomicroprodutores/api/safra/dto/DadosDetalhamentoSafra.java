package br.gov.sp.etec.gestaomicroprodutores.api.safra.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.safra.Safra;

import java.time.LocalDate;

public record DadosDetalhamentoSafra(
        Long idSafra,
        String nome,
        LocalDate dataInicio,
        LocalDate dataFim,
        String status,
        String observacoes,
        Integer totalPlantacoes
) {
    public DadosDetalhamentoSafra(Safra safra) {
        this(
                safra.getIdSafra(),
                safra.getNome(),
                safra.getDataInicio(),
                safra.getDataFim(),
                safra.getStatus(),
                safra.getObservacoes(),
                safra.getPlantacoes() != null ? safra.getPlantacoes().size() : 0
        );
    }
}
