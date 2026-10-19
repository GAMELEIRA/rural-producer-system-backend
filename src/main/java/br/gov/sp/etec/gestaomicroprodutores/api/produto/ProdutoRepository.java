package br.gov.sp.etec.gestaomicroprodutores.api.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT p FROM Produto p WHERE p.idProduto = :id")
    java.util.Optional<Produto> buscarParaMovimentacao(@org.springframework.data.repository.query.Param("id") Long id);

    // Precificação de perdas antes da colheita: só usar automaticamente
    // quando existe uma correspondência inequívoca com a cultura.
    java.util.List<Produto> findByCulturaIdCultura(Long idCultura);

    java.util.Optional<Produto> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdProdutoNot(String nome, Long idProduto);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(p.quantidadeEstoque * p.precoVenda), 0) FROM Produto p")
    java.math.BigDecimal calcularValorTotalEstoqueProdutos();
}
