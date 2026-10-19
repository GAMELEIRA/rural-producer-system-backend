package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosEntradaInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosDetalhamentoMovimentacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.Insumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.InsumoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MovimentacaoInsumoService {
    @Autowired private MovimentacaoInsumoRepository repository;
    @Autowired private InsumoRepository insumoRepository;

    @Transactional(readOnly = true)
    public List<DadosDetalhamentoMovimentacaoInsumo> listar() {
        return repository.listarComInsumos().stream()
                .map(DadosDetalhamentoMovimentacaoInsumo::new).toList();
    }

    @Transactional
    public DadosDetalhamentoMovimentacaoInsumo registrarEntrada(DadosEntradaInsumo dados) {
        Insumo insumo = insumoRepository.buscarParaMovimentacao(dados.idInsumo())
                .orElseThrow(() -> new EntityNotFoundException("Insumo não encontrado: " + dados.idInsumo()));
        insumo.estornarEstoque(dados.quantidade());
        var movimentacao = repository.save(new MovimentacaoInsumo(insumo, "ENTRADA_REPOSICAO",
                dados.quantidade(), dados.origem() == null || dados.origem().isBlank() ?
                        "Reposição de insumo" : dados.origem().trim()));
        return new DadosDetalhamentoMovimentacaoInsumo(movimentacao);
    }
}
