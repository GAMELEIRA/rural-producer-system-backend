package br.gov.sp.etec.gestaomicroprodutores.api.perda;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosAtualizacaoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosCadastroPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosDetalhamentoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.ResumoPerdasPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PerdaService {

    @Autowired
    private PerdaRepository perdaRepository;

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    public List<DadosDetalhamentoPerda> listar() {
        return perdaRepository.findAllComRelacionamentos()
                .stream()
                .map(DadosDetalhamentoPerda::new)
                .toList();
    }

    public DadosDetalhamentoPerda buscarPorId(Long id) {
        return new DadosDetalhamentoPerda(obter(id));
    }

    public List<DadosDetalhamentoPerda> listarPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao)) {
            throw new EntityNotFoundException("Plantação não encontrada com ID: " + idPlantacao);
        }
        return perdaRepository.findByPlantacaoIdPlantacao(idPlantacao)
                .stream()
                .map(DadosDetalhamentoPerda::new)
                .toList();
    }

    public ResumoPerdasPlantacao obterResumoPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao)) {
            throw new EntityNotFoundException("Plantação não encontrada com ID: " + idPlantacao);
        }
        var perdas = perdaRepository.findByPlantacaoIdPlantacao(idPlantacao);
        BigDecimal quantidadeTotal = perdas.stream()
                .map(Perda::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorTotal = perdas.stream()
                .map(p -> p.getValorEstimado() != null ? p.getValorEstimado() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumoPerdasPlantacao(idPlantacao, (long) perdas.size(), quantidadeTotal, valorTotal);
    }

    @Transactional
    public DadosDetalhamentoPerda cadastrar(DadosCadastroPerda dados) {
        Plantacao plantacao = plantacaoRepository.findById(dados.idPlantacao())
                .orElseThrow(() -> new EntityNotFoundException("Plantação não encontrada com ID: " + dados.idPlantacao()));

        if (dados.dataPerda().isBefore(plantacao.getDataPlantio())) {
            throw new RegraDeNegocioException("A data da ocorrência da perda não pode ser anterior à data de plantio (" + plantacao.getDataPlantio() + ")!");
        }

        Perda perda = new Perda(plantacao, dados);
        perdaRepository.save(perda);

        return new DadosDetalhamentoPerda(perda);
    }

    @Transactional
    public DadosDetalhamentoPerda atualizar(Long id, DadosAtualizacaoPerda dados) {
        Perda perda = obter(id);

        if (dados.dataPerda() != null && dados.dataPerda().isBefore(perda.getPlantacao().getDataPlantio())) {
            throw new RegraDeNegocioException("A data da ocorrência da perda não pode ser anterior à data de plantio (" + perda.getPlantacao().getDataPlantio() + ")!");
        }

        perda.atualizarInformacoes(dados);

        return new DadosDetalhamentoPerda(perda);
    }

    @Transactional
    public void excluir(Long id) {
        perdaRepository.delete(obter(id));
    }

    private Perda obter(Long id) {
        return perdaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de perda não encontrado com ID: " + id));
    }
}
