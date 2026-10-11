package br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoMovimentacao(
        Long idMovimentacao,
        Long idProduto,
        String nomeProduto,
        String unidadeMedidaProduto,
        String tipo,
        LocalDateTime dataMovimentacao,
        BigDecimal quantidade,
        BigDecimal saldoAnterior,
        BigDecimal saldoPosterior,
        String origem,
        String observacoes
) {
    public DadosDetalhamentoMovimentacao(MovimentacaoEstoque mov) {
        this(
                mov.getIdMovimentacao(),
                mov.getProduto().getIdProduto(),
                mov.getProduto().getNome(),
                mov.getProduto().getUnidadeMedida(),
                mov.getTipo(),
                mov.getDataMovimentacao(),
                mov.getQuantidade(),
                mov.getSaldoAnterior(),
                mov.getSaldoPosterior(),
                mov.getOrigem(),
                mov.getObservacoes()
        );
    }
}
