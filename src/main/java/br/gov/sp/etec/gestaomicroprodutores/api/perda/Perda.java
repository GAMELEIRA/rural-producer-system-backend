package br.gov.sp.etec.gestaomicroprodutores.api.perda;

import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosAtualizacaoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosCadastroPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "Perda")
@Table(name = "tb_perda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idPerda")
public class Perda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perda")
    private Long idPerda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantacao", nullable = false)
    private Plantacao plantacao;

    @Column(name = "data_perda", nullable = false)
    private LocalDate dataPerda;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantidade;

    @Column(nullable = false, length = 150)
    private String motivo;

    @Column(name = "valor_estimado", precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @Column(length = 500)
    private String observacoes;

    public Perda(Plantacao plantacao, DadosCadastroPerda dados) {
        this.plantacao = plantacao;
        this.dataPerda = dados.dataPerda();
        this.quantidade = dados.quantidade();
        this.motivo = dados.motivo().trim();
        this.valorEstimado = dados.valorEstimado() != null ? dados.valorEstimado() : BigDecimal.ZERO;
        this.observacoes = dados.observacoes();
    }

    public void atualizarInformacoes(DadosAtualizacaoPerda dados) {
        if (dados.dataPerda() != null) {
            this.dataPerda = dados.dataPerda();
        }
        if (dados.quantidade() != null) {
            this.quantidade = dados.quantidade();
        }
        if (dados.motivo() != null && !dados.motivo().isBlank()) {
            this.motivo = dados.motivo().trim();
        }
        if (dados.valorEstimado() != null) {
            this.valorEstimado = dados.valorEstimado();
        }
        if (dados.observacoes() != null) {
            this.observacoes = dados.observacoes();
        }
    }
}
