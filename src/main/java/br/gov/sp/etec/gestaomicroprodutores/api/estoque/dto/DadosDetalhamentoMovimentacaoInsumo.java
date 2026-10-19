package br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoInsumo;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoMovimentacaoInsumo(
        Long idMovimentacao, Long idInsumo, String nomeInsumo,
        String tipo, BigDecimal quantidade, LocalDateTime dataMovimentacao, String origem
) {
    public DadosDetalhamentoMovimentacaoInsumo(MovimentacaoInsumo m) {
        this(m.getIdMovimentacao(), m.getInsumo().getIdInsumo(), m.getInsumo().getNome(),
             m.getTipo(), m.getQuantidade(), m.getDataMovimentacao(), m.getOrigem());
    }
}
