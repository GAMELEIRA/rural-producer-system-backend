package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MovimentacaoInsumoRepository extends JpaRepository<MovimentacaoInsumo, Long> {
    @Query("SELECT m FROM MovimentacaoInsumo m JOIN FETCH m.insumo ORDER BY m.dataMovimentacao DESC, m.idMovimentacao DESC")
    List<MovimentacaoInsumo> listarComInsumos();
}
