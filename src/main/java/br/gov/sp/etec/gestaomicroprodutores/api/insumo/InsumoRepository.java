package br.gov.sp.etec.gestaomicroprodutores.api.insumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT i FROM Insumo i WHERE i.idInsumo = :id")
    java.util.Optional<Insumo> buscarParaMovimentacao(@org.springframework.data.repository.query.Param("id") Long id);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdInsumoNot(String nome, Long idInsumo);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(i.quantidadeEstoque * i.precoUnitario), 0) FROM Insumo i")
    java.math.BigDecimal calcularValorTotalEstoqueInsumos();
}
