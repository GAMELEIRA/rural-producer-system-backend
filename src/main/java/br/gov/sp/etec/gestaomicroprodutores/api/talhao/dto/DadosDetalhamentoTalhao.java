package br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.talhao.Talhao;

import java.math.BigDecimal;

public record DadosDetalhamentoTalhao(
        Long idTalhao,
        String nome,
        BigDecimal area,
        String unidadeArea,
        String localizacao,
        String observacoes
) {
    public DadosDetalhamentoTalhao(Talhao talhao) {
        this(
                talhao.getIdTalhao(),
                talhao.getNome(),
                talhao.getArea(),
                talhao.getUnidadeArea(),
                talhao.getLocalizacao(),
                talhao.getObservacoes()
        );
    }
}
