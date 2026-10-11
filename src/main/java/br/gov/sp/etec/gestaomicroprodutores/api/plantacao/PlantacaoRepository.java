package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantacaoRepository extends JpaRepository<Plantacao, Long> {

    @Query("SELECT p FROM Plantacao p JOIN FETCH p.talhao JOIN FETCH p.cultura")
    List<Plantacao> findAllComRelacionamentos();

    List<Plantacao> findByTalhaoIdTalhao(Long idTalhao);

    List<Plantacao> findByCulturaIdCultura(Long idCultura);

    List<Plantacao> findByStatusIgnoreCase(String status);

    Long countByStatusIgnoreCase(String status);

    Long countByStatusNotIgnoreCase(String status);
}
