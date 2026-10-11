package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query("SELECT m FROM MovimentacaoEstoque m JOIN FETCH m.produto ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findAllComRelacionamentos();

    @Query("SELECT m FROM MovimentacaoEstoque m JOIN FETCH m.produto WHERE m.produto.idProduto = :idProduto ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findByProdutoIdProdutoOrderByDataMovimentacaoDesc(@Param("idProduto") Long idProduto);
}
