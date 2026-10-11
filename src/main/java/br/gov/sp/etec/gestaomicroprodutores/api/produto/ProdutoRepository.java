package br.gov.sp.etec.gestaomicroprodutores.api.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdProdutoNot(String nome, Long idProduto);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(p.quantidadeEstoque * p.precoVenda), 0) FROM Produto p")
    java.math.BigDecimal calcularValorTotalEstoqueProdutos();
}
