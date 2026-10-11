package br.gov.sp.etec.gestaomicroprodutores.api.perda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerdaRepository extends JpaRepository<Perda, Long> {

    @Query("SELECT p FROM Perda p JOIN FETCH p.plantacao pl JOIN FETCH pl.talhao JOIN FETCH pl.cultura")
    List<Perda> findAllComRelacionamentos();

    @Query("SELECT p FROM Perda p JOIN FETCH p.plantacao pl JOIN FETCH pl.talhao JOIN FETCH pl.cultura WHERE pl.idPlantacao = :idPlantacao")
    List<Perda> findByPlantacaoIdPlantacao(@Param("idPlantacao") Long idPlantacao);

    List<Perda> findByMotivoContainingIgnoreCase(String motivo);

    @Query("SELECT COALESCE(SUM(p.valorEstimado), 0) FROM Perda p WHERE p.plantacao.idPlantacao = :idPlantacao")
    java.math.BigDecimal somarValorEstimadoPorPlantacao(@Param("idPlantacao") Long idPlantacao);

    @Query("SELECT COALESCE(SUM(p.valorEstimado), 0) FROM Perda p")
    java.math.BigDecimal somarTotalPerdasEstimadas();
}
