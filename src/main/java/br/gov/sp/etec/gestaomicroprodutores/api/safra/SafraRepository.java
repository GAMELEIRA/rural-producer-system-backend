package br.gov.sp.etec.gestaomicroprodutores.api.safra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafraRepository extends JpaRepository<Safra, Long> {

    @Query("SELECT DISTINCT s FROM Safra s LEFT JOIN FETCH s.plantacoes")
    List<Safra> findAllComPlantacoes();

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdSafraNot(String nome, Long idSafra);

    List<Safra> findByStatusIgnoreCase(String status);

    Long countByStatusIgnoreCase(String status);
}
