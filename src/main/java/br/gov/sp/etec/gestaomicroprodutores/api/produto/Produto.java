package br.gov.sp.etec.gestaomicroprodutores.api.produto;

import br.gov.sp.etec.gestaomicroprodutores.api.produto.dto.DadosCadastroProduto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "Produto")
@Table(name = "tb_produto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idProduto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(name = "unidade_medida", nullable = false, length = 20)
    private String unidadeMedida;
    @Column(name = "preco_venda", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoVenda;
    @Column(name = "quantidade_estoque", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantidadeEstoque;
    @Column(length = 500)
    private String observacoes;

    public Produto(DadosCadastroProduto dados) {
        atualizarInformacoes(dados);
    }

    public void atualizarInformacoes(DadosCadastroProduto dados) {
        this.nome = dados.nome();
        this.unidadeMedida = dados.unidadeMedida();
        this.precoVenda = dados.precoVenda();
        this.quantidadeEstoque = dados.quantidadeEstoque();
        this.observacoes = dados.observacoes();
    }

    public void atualizarDadosCadastrais(DadosCadastroProduto dados) {
        this.nome = dados.nome();
        this.unidadeMedida = dados.unidadeMedida();
        this.precoVenda = dados.precoVenda();
        this.observacoes = dados.observacoes();
    }

    public void adicionarEstoque(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException("Quantidade adicionada ao estoque deve ser maior que zero!");
        }
        this.quantidadeEstoque = this.quantidadeEstoque.add(quantidade);
    }

    public void baixarEstoque(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException("Quantidade para baixa no estoque deve ser maior que zero!");
        }
        if (this.quantidadeEstoque.compareTo(quantidade) < 0) {
            throw new br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException(
                    "Estoque insuficiente do produto '" + this.nome + "'. Disponível: " + this.quantidadeEstoque + " " + this.unidadeMedida);
        }
        this.quantidadeEstoque = this.quantidadeEstoque.subtract(quantidade);
    }
}
