package br.gov.sp.etec.gestaomicroprodutores.api.gasto;

import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosAtualizacaoGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosCadastroGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "GastoProducao")
@Table(name = "tb_gasto_producao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "idGasto")
public class GastoProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto")
    private Long idGasto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantacao", nullable = false)
    private Plantacao plantacao;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_gasto", nullable = false)
    private LocalDate dataGasto;

    @Column(length = 50)
    private String categoria;

    @Column(length = 500)
    private String observacoes;

    public GastoProducao(Plantacao plantacao, DadosCadastroGasto dados) {
        this.plantacao = plantacao;
        this.descricao = dados.descricao().trim();
        this.valor = dados.valor();
        this.dataGasto = dados.dataGasto();
        this.categoria = dados.categoria() != null && !dados.categoria().isBlank()
                ? dados.categoria().trim().toUpperCase()
                : "OUTRO";
        this.observacoes = dados.observacoes();
    }

    public void atualizarInformacoes(DadosAtualizacaoGasto dados) {
        if (dados.descricao() != null && !dados.descricao().isBlank()) {
            this.descricao = dados.descricao().trim();
        }
        if (dados.valor() != null) {
            this.valor = dados.valor();
        }
        if (dados.dataGasto() != null) {
            this.dataGasto = dados.dataGasto();
        }
        if (dados.categoria() != null && !dados.categoria().isBlank()) {
            this.categoria = dados.categoria().trim().toUpperCase();
        }
        if (dados.observacoes() != null) {
            this.observacoes = dados.observacoes();
        }
    }
}
