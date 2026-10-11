package br.gov.sp.etec.gestaomicroprodutores.api.gasto;

import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosAtualizacaoGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosCadastroGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto.DadosDetalhamentoGasto;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GastoProducaoService {

    @Autowired
    private GastoProducaoRepository gastoProducaoRepository;

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    public List<DadosDetalhamentoGasto> listar() {
        return gastoProducaoRepository.findAllComRelacionamentos()
                .stream()
                .map(DadosDetalhamentoGasto::new)
                .toList();
    }

    public DadosDetalhamentoGasto buscarPorId(Long id) {
        GastoProducao gasto = gastoProducaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gasto de produção não encontrado com ID: " + id));
        return new DadosDetalhamentoGasto(gasto);
    }

    public List<DadosDetalhamentoGasto> listarPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao)) {
            throw new EntityNotFoundException("Plantação não encontrada com ID: " + idPlantacao);
        }
        return gastoProducaoRepository.findByPlantacaoIdPlantacaoOrderByDataGastoDesc(idPlantacao)
                .stream()
                .map(DadosDetalhamentoGasto::new)
                .toList();
    }

    @Transactional
    public DadosDetalhamentoGasto cadastrar(DadosCadastroGasto dados) {
        Plantacao plantacao = plantacaoRepository.findById(dados.idPlantacao())
                .orElseThrow(() -> new EntityNotFoundException("Plantação não encontrada com ID: " + dados.idPlantacao()));

        GastoProducao gasto = new GastoProducao(plantacao, dados);
        gastoProducaoRepository.save(gasto);
        return new DadosDetalhamentoGasto(gasto);
    }

    @Transactional
    public DadosDetalhamentoGasto atualizar(Long id, DadosAtualizacaoGasto dados) {
        GastoProducao gasto = gastoProducaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gasto de produção não encontrado com ID: " + id));

        gasto.atualizarInformacoes(dados);
        return new DadosDetalhamentoGasto(gasto);
    }

    @Transactional
    public void excluir(Long id) {
        if (!gastoProducaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Gasto de produção não encontrado com ID: " + id);
        }
        gastoProducaoRepository.deleteById(id);
    }
}
