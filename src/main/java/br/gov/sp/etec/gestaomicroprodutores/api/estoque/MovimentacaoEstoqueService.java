package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosDetalhamentoMovimentacao;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosEntradaEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosSaidaEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<DadosDetalhamentoMovimentacao> listar() {
        return movimentacaoEstoqueRepository.findAllComRelacionamentos()
                .stream()
                .map(DadosDetalhamentoMovimentacao::new)
                .toList();
    }

    public List<DadosDetalhamentoMovimentacao> listarPorProduto(Long idProduto) {
        if (!produtoRepository.existsById(idProduto)) {
            throw new EntityNotFoundException("Produto não encontrado com ID: " + idProduto);
        }
        return movimentacaoEstoqueRepository.findByProdutoIdProdutoOrderByDataMovimentacaoDesc(idProduto)
                .stream()
                .map(DadosDetalhamentoMovimentacao::new)
                .toList();
    }

    @Transactional
    public DadosDetalhamentoMovimentacao registrarEntrada(DadosEntradaEstoque dados) {
        Produto produto = produtoRepository.buscarParaMovimentacao(dados.idProduto())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + dados.idProduto()));

        BigDecimal saldoAnterior = produto.getQuantidadeEstoque();
        produto.adicionarEstoque(dados.quantidade());
        BigDecimal saldoPosterior = produto.getQuantidadeEstoque();

        String tipo = (dados.tipo() != null && !dados.tipo().isBlank()) ? dados.tipo().trim().toUpperCase() : "ENTRADA_COLHEITA";
        String origem = (dados.origem() != null && !dados.origem().isBlank()) ? dados.origem().trim() : "Entrada manual de colheita";

        MovimentacaoEstoque mov = new MovimentacaoEstoque(
                produto,
                tipo,
                LocalDateTime.now(),
                dados.quantidade(),
                saldoAnterior,
                saldoPosterior,
                origem,
                dados.observacoes()
        );

        movimentacaoEstoqueRepository.save(mov);

        return new DadosDetalhamentoMovimentacao(mov);
    }

    @Transactional
    public DadosDetalhamentoMovimentacao registrarSaida(DadosSaidaEstoque dados) {
        Produto produto = produtoRepository.buscarParaMovimentacao(dados.idProduto())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + dados.idProduto()));

        BigDecimal saldoAnterior = produto.getQuantidadeEstoque();
        produto.baixarEstoque(dados.quantidade());
        BigDecimal saldoPosterior = produto.getQuantidadeEstoque();

        String tipo = (dados.tipo() != null && !dados.tipo().isBlank()) ? dados.tipo().trim().toUpperCase() : "SAIDA_AVULSA";
        String origem = (dados.motivo() != null && !dados.motivo().isBlank()) ? dados.motivo().trim() : "Saída manual / Descarte";

        MovimentacaoEstoque mov = new MovimentacaoEstoque(
                produto,
                tipo,
                LocalDateTime.now(),
                dados.quantidade(),
                saldoAnterior,
                saldoPosterior,
                origem,
                dados.observacoes()
        );

        movimentacaoEstoqueRepository.save(mov);

        return new DadosDetalhamentoMovimentacao(mov);
    }
}
