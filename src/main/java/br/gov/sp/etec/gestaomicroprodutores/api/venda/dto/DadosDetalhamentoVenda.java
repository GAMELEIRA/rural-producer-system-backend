package br.gov.sp.etec.gestaomicroprodutores.api.venda.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.venda.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DadosDetalhamentoVenda(
        Long idVenda,
        LocalDateTime dataVenda,
        String cliente,
        BigDecimal valorTotal,
        String formaPagamento,
        String status,
        String observacoes,
        List<DadosDetalhamentoItemVenda> itens
) {
    public DadosDetalhamentoVenda(Venda venda) {
        this(
                venda.getIdVenda(),
                venda.getDataVenda(),
                venda.getCliente(),
                venda.getValorTotal(),
                venda.getFormaPagamento(),
                venda.getStatus(),
                venda.getObservacoes(),
                venda.getItens() != null
                        ? venda.getItens().stream().map(DadosDetalhamentoItemVenda::new).toList()
                        : List.of()
        );
    }
}
