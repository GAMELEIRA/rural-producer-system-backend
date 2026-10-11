package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AplicacaoInsumoRepository extends JpaRepository<AplicacaoInsumo, Long> {

    @Query("SELECT a FROM AplicacaoInsumo a " +
           "JOIN FETCH a.plantacao p " +
           "JOIN FETCH p.talhao " +
           "JOIN FETCH p.cultura " +
           "JOIN FETCH a.insumo")
    List<AplicacaoInsumo> findAllComRelacionamentos();

    @Query("SELECT a FROM AplicacaoInsumo a " +
           "JOIN FETCH a.plantacao p " +
           "JOIN FETCH p.talhao " +
           "JOIN FETCH p.cultura " +
           "JOIN FETCH a.insumo " +
           "WHERE a.plantacao.idPlantacao = :idPlantacao")
    List<AplicacaoInsumo> findByPlantacaoIdPlantacao(@Param("idPlantacao") Long idPlantacao);

    List<AplicacaoInsumo> findByInsumoIdInsumo(Long idInsumo);

    @Query("SELECT COALESCE(SUM(a.custoTotal), 0) FROM AplicacaoInsumo a WHERE a.plantacao.idPlantacao = :idPlantacao")
    BigDecimal somarCustoTotalPorPlantacao(@Param("idPlantacao") Long idPlantacao);

    @Query("SELECT COALESCE(SUM(a.custoTotal), 0) FROM AplicacaoInsumo a")
    BigDecimal somarTotalCustos();
}
