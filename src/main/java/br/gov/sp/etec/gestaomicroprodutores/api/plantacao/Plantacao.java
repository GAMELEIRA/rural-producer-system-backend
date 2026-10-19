package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.Cultura;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosAtualizacaoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosCadastroPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosFinalizarColheita;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.Talhao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "Plantacao")
@Table(name = "tb_plantacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idPlantacao")
public class Plantacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantacao")
    private Long idPlantacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_talhao", nullable = false)
    private Talhao talhao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cultura", nullable = false)
    private Cultura cultura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_safra")
    private br.gov.sp.etec.gestaomicroprodutores.api.safra.Safra safra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto_colheita")
    private br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto produtoColheita;

    @Column(name = "data_plantio", nullable = false)
    private LocalDate dataPlantio;

    @Column(name = "data_colheita")
    private LocalDate dataColheita;

    @Column(name = "quantidade_plantada", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantidadePlantada;

    @Column(name = "quantidade_colhida", precision = 12, scale = 2)
    private BigDecimal quantidadeColhida;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 500)
    private String observacoes;

    public Plantacao(Talhao talhao, Cultura cultura, DadosCadastroPlantacao dados) {
        this.talhao = talhao;
        this.cultura = cultura;
        this.dataPlantio = dados.dataPlantio();
        this.dataColheita = dados.dataColheita();
        this.quantidadePlantada = dados.quantidadePlantada();
        this.quantidadeColhida = dados.quantidadeColhida().subtract(
                dados.quantidadePerdidaColheita() != null ? dados.quantidadePerdidaColheita() : BigDecimal.ZERO);
        this.status = (dados.status() != null && !dados.status().isBlank()) ? dados.status().trim().toUpperCase() : "EM_ANDAMENTO";
        this.observacoes = dados.observacoes();
    }

    public void atualizarInformacoes(Talhao novoTalhao, Cultura novaCultura, DadosAtualizacaoPlantacao dados) {
        if (novoTalhao != null) {
            this.talhao = novoTalhao;
        }
        if (novaCultura != null) {
            this.cultura = novaCultura;
        }
        if (dados.dataPlantio() != null) {
            this.dataPlantio = dados.dataPlantio();
        }
        if (dados.dataColheita() != null) {
            this.dataColheita = dados.dataColheita();
        }
        if (dados.quantidadePlantada() != null) {
            this.quantidadePlantada = dados.quantidadePlantada();
        }
        if (dados.quantidadeColhida() != null) {
            this.quantidadeColhida = dados.quantidadeColhida();
        }
        if (dados.status() != null && !dados.status().isBlank()) {
            this.status = dados.status().trim().toUpperCase();
        }
        if (dados.observacoes() != null) {
            this.observacoes = dados.observacoes();
        }
    }

    public void definirSafra(br.gov.sp.etec.gestaomicroprodutores.api.safra.Safra safra) {
        this.safra = safra;
    }

    public void vincularProdutoColheita(br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto produto) {
        this.produtoColheita = produto;
    }

    public void finalizarColheita(DadosFinalizarColheita dados) {
        this.dataColheita = dados.dataColheita();
        this.quantidadeColhida = dados.quantidadeColhida();
        this.status = "COLHIDA";
        if (dados.observacoes() != null && !dados.observacoes().isBlank()) {
            this.observacoes = (this.observacoes == null || this.observacoes.isBlank()) 
                    ? dados.observacoes() 
                    : this.observacoes + " | Colheita: " + dados.observacoes();
        }
    }
}
