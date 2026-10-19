package br.gov.sp.etec.gestaomicroprodutores.api.venda;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.dto.DadosCadastroVenda;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.dto.DadosDetalhamentoVenda;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.dto.DadosItemVenda;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public List<DadosDetalhamentoVenda> listar() {
        return vendaRepository.findAllComItens()
                .stream()
                .map(DadosDetalhamentoVenda::new)
                .toList();
    }

    public DadosDetalhamentoVenda buscarPorId(Long id) {
        Venda venda = vendaRepository.findByIdComItens(id)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com ID: " + id));
        return new DadosDetalhamentoVenda(venda);
    }

    public List<DadosDetalhamentoVenda> listarPorStatus(String status) {
        return vendaRepository.findByStatusOrderByDataVendaDesc(status.toUpperCase())
                .stream()
                .map(DadosDetalhamentoVenda::new)
                .toList();
    }

    public List<DadosDetalhamentoVenda> listarPorCliente(String cliente) {
        return vendaRepository.findByClienteContainingIgnoreCaseOrderByDataVendaDesc(cliente)
                .stream()
                .map(DadosDetalhamentoVenda::new)
                .toList();
    }

    @Transactional
    public DadosDetalhamentoVenda cadastrar(DadosCadastroVenda dados) {
        if (dados.itens() == null || dados.itens().isEmpty()) {
            throw new RegraDeNegocioException("A venda deve conter ao menos um item de produto!");
        }

        Venda venda = new Venda(dados.dataVenda(), dados.cliente(), dados.formaPagamento(), dados.observacoes());

        for (DadosItemVenda itemDto : dados.itens()) {
            Produto produto = produtoRepository.buscarParaMovimentacao(itemDto.idProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemDto.idProduto()));

            BigDecimal saldoAnterior = produto.getQuantidadeEstoque();
            // Baixa no estoque do produto (valida se o saldo é suficiente)
            produto.baixarEstoque(itemDto.quantidade());
            BigDecimal saldoPosterior = produto.getQuantidadeEstoque();

            // Auditoria de movimentação de estoque
            MovimentacaoEstoque mov = new MovimentacaoEstoque(
                    produto,
                    "SAIDA_VENDA",
                    LocalDateTime.now(),
                    itemDto.quantidade(),
                    saldoAnterior,
                    saldoPosterior,
                    "Venda para " + (venda.getCliente() != null ? venda.getCliente() : "Consumidor"),
                    "Saída por venda a R$ " + itemDto.precoUnitario() + " / " + produto.getUnidadeMedida()
            );
            movimentacaoEstoqueRepository.save(mov);

            ItemVenda itemVenda = new ItemVenda(venda, produto, itemDto.quantidade(), itemDto.precoUnitario());
            venda.adicionarItem(itemVenda);
        }

        vendaRepository.save(venda);
        return new DadosDetalhamentoVenda(venda);
    }


    @Transactional
    public DadosDetalhamentoVenda atualizar(Long id, DadosCadastroVenda dados) {
        Venda venda = vendaRepository.findByIdComItens(id)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com ID: " + id));
        if (!"CONCLUIDA".equals(venda.getStatus())) {
            throw new RegraDeNegocioException("Somente vendas concluídas podem ser corrigidas.");
        }
        if (dados.itens() == null || dados.itens().isEmpty()) {
            throw new RegraDeNegocioException("A venda precisa ter itens.");
        }
        // Devolve o saldo dos itens antigos antes de debitar os novos, na mesma transação.
        for (ItemVenda antigo : venda.getItens()) {
            Produto produto = produtoRepository.buscarParaMovimentacao(antigo.getProduto().getIdProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
            BigDecimal anterior = produto.getQuantidadeEstoque();
            produto.adicionarEstoque(antigo.getQuantidade());
            movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(produto, "ESTORNO_CORRECAO_VENDA",
                    LocalDateTime.now(), antigo.getQuantidade(), anterior, produto.getQuantidadeEstoque(),
                    "Correção da venda #" + id, null));
        }
        venda.getItens().clear();
        venda.setCliente(dados.cliente() == null || dados.cliente().isBlank() ? "Consumidor Final" : dados.cliente().trim());
        venda.setFormaPagamento(dados.formaPagamento());
        if (dados.dataVenda() != null) venda.setDataVenda(dados.dataVenda());
        venda.setObservacoes(dados.observacoes());
        for (DadosItemVenda item : dados.itens()) {
            Produto produto = produtoRepository.buscarParaMovimentacao(item.idProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + item.idProduto()));
            BigDecimal anterior = produto.getQuantidadeEstoque();
            produto.baixarEstoque(item.quantidade());
            movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(produto, "SAIDA_CORRECAO_VENDA",
                    LocalDateTime.now(), item.quantidade(), anterior, produto.getQuantidadeEstoque(),
                    "Correção da venda #" + id, null));
            venda.adicionarItem(new ItemVenda(venda, produto, item.quantidade(), item.precoUnitario()));
        }
        return new DadosDetalhamentoVenda(venda);
    }

    @Transactional
    public DadosDetalhamentoVenda cancelar(Long id) {
        Venda venda = vendaRepository.findByIdComItens(id)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com ID: " + id));

        venda.cancelar();

        // Estorno de estoque para cada item
        for (ItemVenda item : venda.getItens()) {
            Produto produto = item.getProduto();
            BigDecimal saldoAnterior = produto.getQuantidadeEstoque();
            produto.adicionarEstoque(item.getQuantidade());
            BigDecimal saldoPosterior = produto.getQuantidadeEstoque();

            MovimentacaoEstoque mov = new MovimentacaoEstoque(
                    produto,
                    "ENTRADA_CANCELAMENTO_VENDA",
                    LocalDateTime.now(),
                    item.getQuantidade(),
                    saldoAnterior,
                    saldoPosterior,
                    "Cancelamento da Venda #" + venda.getIdVenda(),
                    "Estorno de estoque por cancelamento de venda"
            );
            movimentacaoEstoqueRepository.save(mov);
        }

        return new DadosDetalhamentoVenda(venda);
    }
}
