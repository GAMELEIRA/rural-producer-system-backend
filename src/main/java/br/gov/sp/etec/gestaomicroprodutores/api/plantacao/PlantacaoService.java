package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.Cultura;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.CulturaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosAtualizacaoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosCadastroPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosDetalhamentoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosFinalizarColheita;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.Talhao;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.TalhaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlantacaoService {

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private CulturaRepository culturaRepository;

    public List<DadosDetalhamentoPlantacao> listar() {
        return plantacaoRepository.findAllComRelacionamentos()
                .stream()
                .map(DadosDetalhamentoPlantacao::new)
                .toList();
    }

    public DadosDetalhamentoPlantacao buscarPorId(Long id) {
        return new DadosDetalhamentoPlantacao(obter(id));
    }

    public List<DadosDetalhamentoPlantacao> listarPorTalhao(Long idTalhao) {
        if (!talhaoRepository.existsById(idTalhao)) {
            throw new EntityNotFoundException("Talhão não encontrado com ID: " + idTalhao);
        }
        return plantacaoRepository.findByTalhaoIdTalhao(idTalhao)
                .stream()
                .map(DadosDetalhamentoPlantacao::new)
                .toList();
    }

    public List<DadosDetalhamentoPlantacao> listarPorStatus(String status) {
        return plantacaoRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(DadosDetalhamentoPlantacao::new)
                .toList();
    }

    @Transactional
    public DadosDetalhamentoPlantacao cadastrar(DadosCadastroPlantacao dados) {
        Talhao talhao = talhaoRepository.findById(dados.idTalhao())
                .orElseThrow(() -> new EntityNotFoundException("Talhão não encontrado com ID: " + dados.idTalhao()));

        Cultura cultura = culturaRepository.findById(dados.idCultura())
                .orElseThrow(() -> new EntityNotFoundException("Cultura não encontrada com ID: " + dados.idCultura()));

        validarDatas(dados.dataPlantio(), dados.dataColheita());

        if (dados.quantidadeColhida() != null && dados.quantidadeColhida().compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException("A quantidade colhida não pode ser negativa!");
        }

        Plantacao plantacao = new Plantacao(talhao, cultura, dados);
        plantacaoRepository.save(plantacao);

        return new DadosDetalhamentoPlantacao(plantacao);
    }

    @Transactional
    public DadosDetalhamentoPlantacao atualizar(Long id, DadosAtualizacaoPlantacao dados) {
        Plantacao plantacao = obter(id);

        Talhao novoTalhao = null;
        if (dados.idTalhao() != null && !dados.idTalhao().equals(plantacao.getTalhao().getIdTalhao())) {
            novoTalhao = talhaoRepository.findById(dados.idTalhao())
                    .orElseThrow(() -> new EntityNotFoundException("Talhão não encontrado com ID: " + dados.idTalhao()));
        }

        Cultura novaCultura = null;
        if (dados.idCultura() != null && !dados.idCultura().equals(plantacao.getCultura().getIdCultura())) {
            novaCultura = culturaRepository.findById(dados.idCultura())
                    .orElseThrow(() -> new EntityNotFoundException("Cultura não encontrada com ID: " + dados.idCultura()));
        }

        LocalDate dataPlantioFinal = dados.dataPlantio() != null ? dados.dataPlantio() : plantacao.getDataPlantio();
        LocalDate dataColheitaFinal = dados.dataColheita() != null ? dados.dataColheita() : plantacao.getDataColheita();
        validarDatas(dataPlantioFinal, dataColheitaFinal);

        if (dados.quantidadeColhida() != null && dados.quantidadeColhida().compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException("A quantidade colhida não pode ser negativa!");
        }

        plantacao.atualizarInformacoes(novoTalhao, novaCultura, dados);

        return new DadosDetalhamentoPlantacao(plantacao);
    }

    @Transactional
    public DadosDetalhamentoPlantacao finalizarColheita(Long id, DadosFinalizarColheita dados) {
        Plantacao plantacao = obter(id);

        if (dados.dataColheita().isBefore(plantacao.getDataPlantio())) {
            throw new RegraDeNegocioException("A data da colheita não pode ser anterior à data de plantio (" + plantacao.getDataPlantio() + ")!");
        }

        plantacao.finalizarColheita(dados);

        return new DadosDetalhamentoPlantacao(plantacao);
    }

    @Transactional
    public void excluir(Long id) {
        plantacaoRepository.delete(obter(id));
    }

    private Plantacao obter(Long id) {
        return plantacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plantação não encontrada com ID: " + id));
    }

    private void validarDatas(LocalDate dataPlantio, LocalDate dataColheita) {
        if (dataPlantio != null && dataColheita != null && dataColheita.isBefore(dataPlantio)) {
            throw new RegraDeNegocioException("A data de colheita não pode ser anterior à data de plantio!");
        }
    }
}
