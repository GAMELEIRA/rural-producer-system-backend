package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoService;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.PerdaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.Cultura;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.CulturaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosAtualizacaoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosCadastroPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosDetalhamentoPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosFinalizarColheita;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import java.time.LocalDateTime;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.Safra;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.SafraRepository;
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

    @Autowired
    private SafraRepository safraRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private AplicacaoInsumoService aplicacaoInsumoService;
    @Autowired
    private AplicacaoInsumoRepository aplicacaoInsumoRepository;
    @Autowired
    private GastoProducaoRepository gastoProducaoRepository;
    @Autowired
    private PerdaRepository perdaRepository;

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

        Safra safra = obterSafraAtiva(dados.idSafra());
        validarPeriodoSafra(safra, dados.dataPlantio(), dados.dataColheita());
        Plantacao plantacao = new Plantacao(talhao, cultura, dados);
        plantacao.definirSafra(safra);
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

        Safra safra = dados.idSafra() != null ? obterSafraAtiva(dados.idSafra()) : plantacao.getSafra();
        if (safra != null) validarPeriodoSafra(safra, dataPlantioFinal, dataColheitaFinal);
        plantacao.atualizarInformacoes(novoTalhao, novaCultura, dados);
        if (dados.idSafra() != null) plantacao.definirSafra(safra);

        return new DadosDetalhamentoPlantacao(plantacao);
    }

    @Transactional
    public DadosDetalhamentoPlantacao finalizarColheita(Long id, DadosFinalizarColheita dados) {
        Plantacao plantacao = obter(id);

        if ("COLHIDA".equalsIgnoreCase(plantacao.getStatus()) || "COLHIDO".equalsIgnoreCase(plantacao.getStatus())) {
            throw new RegraDeNegocioException("Esta plantação já foi colhida!");
        }
        if (plantacao.getSafra() != null) {
            validarPeriodoSafra(plantacao.getSafra(), plantacao.getDataPlantio(), dados.dataColheita());
        }
        if (dados.dataColheita().isBefore(plantacao.getDataPlantio())) {
            throw new RegraDeNegocioException("A data da colheita não pode ser anterior à data de plantio (" + plantacao.getDataPlantio() + ")!");
        }

        Produto produto = produtoRepository.buscarParaMovimentacao(dados.idProduto())
                .orElseThrow(() -> new EntityNotFoundException("Produto da colheita não encontrado"));
        if (dados.quantidadeColhida().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal anterior = produto.getQuantidadeEstoque();
            produto.adicionarEstoque(dados.quantidadeColhida());
            movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(produto,
                "ENTRADA_COLHEITA", LocalDateTime.now(), dados.quantidadeColhida(),
                anterior, produto.getQuantidadeEstoque(), "Plantação #" + plantacao.getIdPlantacao(),
                "Colheita da cultura " + plantacao.getCultura().getNome()));
        }
        plantacao.finalizarColheita(dados);

        return new DadosDetalhamentoPlantacao(plantacao);
    }

    @Transactional
    public void excluir(Long id) {
        Plantacao plantacao = obter(id);

        // Uma colheita movimenta estoque compartilhado por outras operacoes.
        // Reverter somente as entradas identificadas como sendo desta plantacao.
        var colheitas = movimentacaoEstoqueRepository.findColheitasPorOrigem("Plantação #" + id);
        for (MovimentacaoEstoque colheita : colheitas) {
            Produto produto = produtoRepository.buscarParaMovimentacao(colheita.getProduto().getIdProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto da colheita não encontrado"));
            if (produto.getQuantidadeEstoque().compareTo(colheita.getQuantidade()) < 0) {
                throw new RegraDeNegocioException(
                        "Não é possível excluir a plantação: a colheita já foi movimentada ou vendida e o estoque disponível de '" +
                        produto.getNome() + "' é insuficiente para o estorno. Regularize as operações antes de excluir.");
            }
            produto.baixarEstoque(colheita.getQuantidade());
            movimentacaoEstoqueRepository.delete(colheita);
        }
        movimentacaoEstoqueRepository.flush();

        // Apagar aplicacoes usando a regra existente devolve insumos ao estoque.
        aplicacaoInsumoRepository.findByPlantacaoIdPlantacao(id).forEach(aplicacao ->
                aplicacaoInsumoService.excluir(aplicacao.getIdAplicacao()));
        aplicacaoInsumoRepository.flush();

        // Retirar somente gastos e perdas diretamente associados a plantacao.
        gastoProducaoRepository.deleteAll(gastoProducaoRepository.findByPlantacaoIdPlantacaoOrderByDataGastoDesc(id));
        gastoProducaoRepository.flush();
        perdaRepository.deleteAll(perdaRepository.findByPlantacaoIdPlantacao(id));
        perdaRepository.flush();

        // A transacao reverte todos os passos se qualquer operacao falhar.
        plantacaoRepository.delete(plantacao);
        plantacaoRepository.flush();
    }

    private Safra obterSafraAtiva(Long id) {
        Safra safra = safraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Safra não encontrada com ID: " + id));
        if ("FINALIZADA".equalsIgnoreCase(safra.getStatus())) {
            throw new RegraDeNegocioException("Não é possível vincular plantações a uma safra finalizada!");
        }
        return safra;
    }

    private void validarPeriodoSafra(Safra safra, LocalDate plantio, LocalDate colheita) {
        if (plantio.isBefore(safra.getDataInicio()) || plantio.isAfter(safra.getDataFim())
                || (colheita != null && (colheita.isBefore(safra.getDataInicio()) || colheita.isAfter(safra.getDataFim())))) {
            throw new RegraDeNegocioException("As datas do cultivo devem estar dentro do período da safra!");
        }
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
