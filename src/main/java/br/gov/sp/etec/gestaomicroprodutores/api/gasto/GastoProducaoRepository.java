package br.gov.sp.etec.gestaomicroprodutores.api.gasto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GastoProducaoRepository extends JpaRepository<GastoProducao, Long> {

    @Query("SELECT g FROM GastoProducao g JOIN FETCH g.plantacao p JOIN FETCH p.cultura JOIN FETCH p.talhao ORDER BY g.dataGasto DESC")
    List<GastoProducao> findAllComRelacionamentos();

    List<GastoProducao> findByPlantacaoIdPlantacaoOrderByDataGastoDesc(Long idPlantacao);

    @Query("SELECT COALESCE(SUM(g.valor), 0) FROM GastoProducao g WHERE g.plantacao.idPlantacao = :idPlantacao")
    BigDecimal somarGastosPorPlantacao(@Param("idPlantacao") Long idPlantacao);

    @Query("SELECT COALESCE(SUM(g.valor), 0) FROM GastoProducao g")
    BigDecimal somarTotalGastos();
}
