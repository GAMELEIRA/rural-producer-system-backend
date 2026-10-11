package br.gov.sp.etec.gestaomicroprodutores.api.insumo;

import br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto.DadosCadastroInsumo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "Insumo")
@Table(name = "tb_insumo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idInsumo")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Long idInsumo;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 50)
    private String tipo;
    @Column(name = "unidade_medida", nullable = false, length = 20)
    private String unidadeMedida;
    @Column(name = "quantidade_estoque", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantidadeEstoque;
    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;
    @Column(length = 500)
    private String observacoes;

    public Insumo(DadosCadastroInsumo dados) {
        atualizarInformacoes(dados);
    }

    public void atualizarInformacoes(DadosCadastroInsumo dados) {
        this.nome = dados.nome();
        this.tipo = dados.tipo();
        this.unidadeMedida = dados.unidadeMedida();
        this.quantidadeEstoque = dados.quantidadeEstoque();
        this.precoUnitario = dados.precoUnitario();
        this.observacoes = dados.observacoes();
    }

    public void debitarEstoque(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException("A quantidade aplicada deve ser maior que zero!");
        }
        if (this.quantidadeEstoque.compareTo(quantidade) < 0) {
            throw new br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException(
                    "Estoque insuficiente para o insumo '" + this.nome + "'. Disponível: " + this.quantidadeEstoque + " " + this.unidadeMedida);
        }
        this.quantidadeEstoque = this.quantidadeEstoque.subtract(quantidade);
    }

    public void estornarEstoque(BigDecimal quantidade) {
        if (quantidade != null && quantidade.compareTo(BigDecimal.ZERO) > 0) {
            this.quantidadeEstoque = this.quantidadeEstoque.add(quantidade);
        }
    }
}
