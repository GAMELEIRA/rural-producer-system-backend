package br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.Cultura;

public record DadosDetalhamentoCultura(
        Long idCultura,
        String nome,
        String tipo,
        Integer cicloEstimado,
        String unidadeCiclo,
        String observacoes
) {
    public DadosDetalhamentoCultura(Cultura cultura) {
        this(
                cultura.getIdCultura(),
                cultura.getNome(),
                cultura.getTipo(),
                cultura.getCicloEstimado(),
                cultura.getUnidadeCiclo(),
                cultura.getObservacoes()
        );
    }
}
