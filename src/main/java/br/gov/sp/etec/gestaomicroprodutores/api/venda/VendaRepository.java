package br.gov.sp.etec.gestaomicroprodutores.api.venda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens i LEFT JOIN FETCH i.produto ORDER BY v.dataVenda DESC")
    List<Venda> findAllComItens();

    @Query("SELECT v FROM Venda v LEFT JOIN FETCH v.itens i LEFT JOIN FETCH i.produto WHERE v.idVenda = :id")
    Optional<Venda> findByIdComItens(@Param("id") Long id);

    List<Venda> findByStatusOrderByDataVendaDesc(String status);

    List<Venda> findByClienteContainingIgnoreCaseOrderByDataVendaDesc(String cliente);

    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v WHERE v.status = 'CONCLUIDA'")
    BigDecimal somarTotalVendasConcluidas();

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.status = 'CONCLUIDA'")
    Long contarVendasConcluidas();
}
