package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "MovimentacaoEstoque")
@Table(name = "tb_movimentacao_estoque")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idMovimentacao")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao")
    private Long idMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "saldo_anterior", nullable = false, precision = 14, scale = 3)
    private BigDecimal saldoAnterior;

    @Column(name = "saldo_posterior", nullable = false, precision = 14, scale = 3)
    private BigDecimal saldoPosterior;

    @Column(length = 100)
    private String origem;

    @Column(length = 500)
    private String observacoes;

    public MovimentacaoEstoque(
            Produto produto,
            String tipo,
            LocalDateTime dataMovimentacao,
            BigDecimal quantidade,
            BigDecimal saldoAnterior,
            BigDecimal saldoPosterior,
            String origem,
            String observacoes
    ) {
        this.produto = produto;
        this.tipo = tipo;
        this.dataMovimentacao = dataMovimentacao;
        this.quantidade = quantidade;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.origem = origem;
        this.observacoes = observacoes;
    }
}
