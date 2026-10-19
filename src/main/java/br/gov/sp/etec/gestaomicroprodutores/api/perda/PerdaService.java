package br.gov.sp.etec.gestaomicroprodutores.api.perda;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosAtualizacaoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosCadastroPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosDetalhamentoPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.ResumoPerdasPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PerdaService {
    @Autowired private PerdaRepository perdaRepository;
    @Autowired private PlantacaoRepository plantacaoRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private MovimentacaoEstoqueRepository movimentacoes;

    @Transactional(readOnly = true)
    public List<DadosDetalhamentoPerda> listar() {
        return perdaRepository.findAllComRelacionamentos().stream().map(DadosDetalhamentoPerda::new).toList();
    }
    @Transactional(readOnly = true)
    public DadosDetalhamentoPerda buscarPorId(Long id) { return new DadosDetalhamentoPerda(obter(id)); }

    @Transactional(readOnly = true)
    public List<DadosDetalhamentoPerda> listarPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao))
            throw new EntityNotFoundException("Plantação não encontrada: " + idPlantacao);
        return perdaRepository.findByPlantacaoIdPlantacao(idPlantacao)
                .stream().map(DadosDetalhamentoPerda::new).toList();
    }

    @Transactional(readOnly = true)
    public ResumoPerdasPlantacao obterResumoPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao))
            throw new EntityNotFoundException("Plantação não encontrada: " + idPlantacao);
        var perdas = perdaRepository.findByPlantacaoIdPlantacao(idPlantacao);
        BigDecimal quantidade = perdas.stream().map(Perda::getQuantidade).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valor = perdas.stream().map(p -> p.getValorEstimado() == null ?
                BigDecimal.ZERO : p.getValorEstimado()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ResumoPerdasPlantacao(idPlantacao, (long) perdas.size(), quantidade, valor);
    }

    @Transactional
    public DadosDetalhamentoPerda cadastrar(DadosCadastroPerda dados) {
        Plantacao plantacao = plantacaoRepository.findById(dados.idPlantacao())
                .orElseThrow(() -> new EntityNotFoundException("Plantação não encontrada: " + dados.idPlantacao()));
        validarData(plantacao, dados.dataPerda());
        boolean colhida = plantacao.getStatus().equalsIgnoreCase("COLHIDA")
                || plantacao.getStatus().equalsIgnoreCase("COLHIDO");
        if (colhida && dados.dataPerda().isBefore(plantacao.getDataColheita()))
            throw new RegraDeNegocioException("Perda após colheita deve ter data igual ou posterior à colheita.");
        Perda perda = new Perda(plantacao, dados);
        if (colhida) perda.definirEtapa("POS_COLHEITA");
        perda = perdaRepository.save(perda);
        if (colhida) movimentarEstoque(perda, dados.quantidade(), true,
                "SAIDA_PERDA", "Perda #" + perda.getIdPerda());
        return new DadosDetalhamentoPerda(perda);
    }

    @Transactional
    public DadosDetalhamentoPerda atualizar(Long id, DadosAtualizacaoPerda dados) {
        Perda perda = obter(id);
        if ("NA_COLHEITA".equals(perda.getEtapa()))
            throw new RegraDeNegocioException("Perda registrada na colheita: corrija a colheita antes de alterá-la.");
        LocalDate data = dados.dataPerda() != null ? dados.dataPerda() : perda.getDataPerda();
        validarData(perda.getPlantacao(), data);
        if ("POS_COLHEITA".equals(perda.getEtapa()) &&
                data.isBefore(perda.getPlantacao().getDataColheita()))
            throw new RegraDeNegocioException("A data de perda deve ser posterior à colheita.");
        if (dados.quantidade() != null && "POS_COLHEITA".equals(perda.getEtapa())) {
            BigDecimal diferenca = dados.quantidade().subtract(perda.getQuantidade());
            if (diferenca.signum() != 0)
                movimentarEstoque(perda, diferenca.abs(), diferenca.signum() > 0,
                        diferenca.signum() > 0 ? "SAIDA_AJUSTE_PERDA" : "ENTRADA_AJUSTE_PERDA",
                        "Ajuste da perda #" + id);
        }
        perda.atualizarInformacoes(dados);
        return new DadosDetalhamentoPerda(perda);
    }

    @Transactional
    public void excluir(Long id) {
        Perda perda = obter(id);
        if ("NA_COLHEITA".equals(perda.getEtapa()))
            throw new RegraDeNegocioException("Esta perda faz parte da colheita finalizada e não pode ser removida isoladamente.");
        if ("POS_COLHEITA".equals(perda.getEtapa())) {
            movimentarEstoque(perda, perda.getQuantidade(), false,
                    "ENTRADA_ESTORNO_PERDA", "Exclusão da perda #" + id);
        }
        perdaRepository.delete(perda);
    }

    private void movimentarEstoque(Perda perda, BigDecimal qtd, boolean saida, String tipo, String origem) {
        Produto produtoColheita = perda.getPlantacao().getProdutoColheita();
        if (produtoColheita == null)
            throw new RegraDeNegocioException(
                "Esta plantação não tem um produto de colheita vinculado. Regularize a colheita antes de lançar a perda.");
        Produto produto = produtoRepository.buscarParaMovimentacao(produtoColheita.getIdProduto())
                .orElseThrow(() -> new EntityNotFoundException("Produto de colheita não encontrado."));
        BigDecimal anterior = produto.getQuantidadeEstoque();
        if (saida) produto.baixarEstoque(qtd);
        else produto.adicionarEstoque(qtd);
        movimentacoes.save(new MovimentacaoEstoque(produto, tipo, LocalDateTime.now(), qtd,
                anterior, produto.getQuantidadeEstoque(), origem,
                "Plantação #" + perda.getPlantacao().getIdPlantacao()));
    }

    private void validarData(Plantacao plantacao, LocalDate data) {
        if (data.isBefore(plantacao.getDataPlantio()))
            throw new RegraDeNegocioException("A perda não pode ser anterior à data de plantio.");
    }

    private Perda obter(Long id) {
        return perdaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perda não encontrada: " + id));
    }
}
