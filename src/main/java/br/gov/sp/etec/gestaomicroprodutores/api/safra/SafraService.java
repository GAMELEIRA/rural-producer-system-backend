package br.gov.sp.etec.gestaomicroprodutores.api.safra;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosAtualizacaoSafra;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosCadastroSafra;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosDetalhamentoSafra;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SafraService {

    @Autowired
    private SafraRepository safraRepository;

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    public List<DadosDetalhamentoSafra> listar() {
        return safraRepository.findAllComPlantacoes()
                .stream()
                .map(DadosDetalhamentoSafra::new)
                .toList();
    }

    public DadosDetalhamentoSafra buscarPorId(Long id) {
        return new DadosDetalhamentoSafra(obter(id));
    }

    public List<DadosDetalhamentoSafra> listarPorStatus(String status) {
        return safraRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(DadosDetalhamentoSafra::new)
                .toList();
    }

    @Transactional
    public DadosDetalhamentoSafra cadastrar(DadosCadastroSafra dados) {
        if (safraRepository.existsByNomeIgnoreCase(dados.nome().trim())) {
            throw new RegraDeNegocioException("Já existe uma safra cadastrada com o nome '" + dados.nome().trim() + "'!");
        }

        validarDatas(dados.dataInicio(), dados.dataFim());

        Safra safra = new Safra(dados);
        safraRepository.save(safra);

        return new DadosDetalhamentoSafra(safra);
    }

    @Transactional
    public DadosDetalhamentoSafra atualizar(Long id, DadosAtualizacaoSafra dados) {
        Safra safra = obter(id);

        if (dados.nome() != null && !dados.nome().isBlank() 
                && safraRepository.existsByNomeIgnoreCaseAndIdSafraNot(dados.nome().trim(), id)) {
            throw new RegraDeNegocioException("Já existe outra safra cadastrada com o nome '" + dados.nome().trim() + "'!");
        }

        LocalDate dataInicioFinal = dados.dataInicio() != null ? dados.dataInicio() : safra.getDataInicio();
        LocalDate dataFimFinal = dados.dataFim() != null ? dados.dataFim() : safra.getDataFim();
        validarDatas(dataInicioFinal, dataFimFinal);

        safra.atualizarInformacoes(dados);

        return new DadosDetalhamentoSafra(safra);
    }

    @Transactional
    public DadosDetalhamentoSafra finalizar(Long id) {
        Safra safra = obter(id);
        boolean pendentes = plantacaoRepository.findAll().stream()
                .anyMatch(p -> p.getSafra() != null && id.equals(p.getSafra().getIdSafra())
                        && !"COLHIDA".equalsIgnoreCase(p.getStatus()) && !"COLHIDO".equalsIgnoreCase(p.getStatus()));
        if (pendentes) throw new RegraDeNegocioException("Finalize as colheitas antes de encerrar a safra!");
        safra.finalizarSafra();
        return new DadosDetalhamentoSafra(safra);
    }

    @Transactional
    public void excluir(Long id) {
        Safra safra = obter(id);
        if (safra.getPlantacoes() != null && !safra.getPlantacoes().isEmpty()) {
            throw new RegraDeNegocioException("Não é possível excluir uma safra que possui plantações associadas!");
        }
        safraRepository.delete(safra);
    }

    private Safra obter(Long id) {
        return safraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Safra não encontrada com ID: " + id));
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException("A data de término da safra não pode ser anterior à data de início!");
        }
    }
}
