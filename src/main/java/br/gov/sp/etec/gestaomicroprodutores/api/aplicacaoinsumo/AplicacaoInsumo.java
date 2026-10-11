package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo;

import br.gov.sp.etec.gestaomicroprodutores.api.insumo.Insumo;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "AplicacaoInsumo")
@Table(name = "tb_aplicacao_insumo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idAplicacao")
public class AplicacaoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aplicacao")
    private Long idAplicacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantacao", nullable = false)
    private Plantacao plantacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false)
    private Insumo insumo;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "custo_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotal;

    @Column(length = 500)
    private String observacoes;

    public AplicacaoInsumo(Plantacao plantacao, Insumo insumo, LocalDate dataAplicacao, BigDecimal quantidade, BigDecimal custoTotal, String observacoes) {
        this.plantacao = plantacao;
        this.insumo = insumo;
        this.dataAplicacao = dataAplicacao;
        this.quantidade = quantidade;
        this.custoTotal = custoTotal;
        this.observacoes = observacoes;
    }

    public void atualizarInformacoes(LocalDate dataAplicacao, BigDecimal novaQuantidade, BigDecimal novoCustoTotal, String observacoes) {
        if (dataAplicacao != null) {
            this.dataAplicacao = dataAplicacao;
        }
        if (novaQuantidade != null) {
            this.quantidade = novaQuantidade;
        }
        if (novoCustoTotal != null) {
            this.custoTotal = novoCustoTotal;
        }
        if (observacoes != null) {
            this.observacoes = observacoes;
        }
    }
}
