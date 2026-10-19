package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import br.gov.sp.etec.gestaomicroprodutores.api.insumo.Insumo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "MovimentacaoInsumo")
@Table(name = "tb_movimentacao_insumo")
@Getter
@NoArgsConstructor
public class MovimentacaoInsumo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao")
    private Long idMovimentacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false)
    private Insumo insumo;
    @Column(nullable = false, length = 40)
    private String tipo;
    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantidade;
    @Column(nullable = false, length = 200)
    private String origem;

    public MovimentacaoInsumo(Insumo insumo, String tipo, BigDecimal quantidade, String origem) {
        this.insumo = insumo;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.origem = origem;
        this.dataMovimentacao = LocalDateTime.now();
    }
}
