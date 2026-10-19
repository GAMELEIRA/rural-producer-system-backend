package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoService;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.PerdaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantacaoServiceExclusaoTest {
    @InjectMocks private PlantacaoService service;
    @Mock private PlantacaoRepository plantacoes;
    @Mock private AplicacaoInsumoRepository aplicacoes;
    @Mock private AplicacaoInsumoService aplicacaoService;
    @Mock private GastoProducaoRepository gastos;
    @Mock private PerdaRepository perdas;
    @Mock private ProdutoRepository produtos;
    @Mock private MovimentacaoEstoqueRepository movimentacoes;

    private Plantacao plantacao;

    @BeforeEach
    void preparar() {
        plantacao = mock(Plantacao.class);
        when(plantacoes.findById(1L)).thenReturn(Optional.of(plantacao));
    }

    @Test
    void excluiPlantacaoSemOperacoes() {
        service.excluir(1L);
        verify(plantacoes).delete(plantacao);
        verify(plantacoes).flush();
        verify(aplicacoes).flush();
        verify(gastos).flush();
        verify(perdas).flush();
    }

    @Test
    void bloqueiaEstornoDeColheitaSemEstoqueDisponivel() {
        Produto produto = mock(Produto.class);
        MovimentacaoEstoque colheita = mock(MovimentacaoEstoque.class);
        when(colheita.getProduto()).thenReturn(produto);
        when(produto.getIdProduto()).thenReturn(4L);
        when(produtos.buscarParaMovimentacao(4L)).thenReturn(Optional.of(produto));
        when(colheita.getQuantidade()).thenReturn(new BigDecimal("10.00"));
        when(produto.getQuantidadeEstoque()).thenReturn(new BigDecimal("2.00"));
        when(produto.getNome()).thenReturn("Milho");
        when(movimentacoes.findColheitasPorOrigem("Plantação #1")).thenReturn(List.of(colheita));

        assertThrows(RegraDeNegocioException.class, () -> service.excluir(1L));

        verify(plantacoes, never()).delete(any());
        verify(movimentacoes, never()).delete(any());
        verify(produto, never()).baixarEstoque(any());
    }

    @Test
    void estornaEntradaDeColheitaAntesDeExcluirPlantacao() {
        Produto produto = mock(Produto.class);
        MovimentacaoEstoque colheita = mock(MovimentacaoEstoque.class);
        when(colheita.getProduto()).thenReturn(produto);
        when(produto.getIdProduto()).thenReturn(4L);
        when(produtos.buscarParaMovimentacao(4L)).thenReturn(Optional.of(produto));
        when(colheita.getQuantidade()).thenReturn(new BigDecimal("10.00"));
        when(produto.getQuantidadeEstoque()).thenReturn(new BigDecimal("12.00"));
        when(movimentacoes.findColheitasPorOrigem("Plantação #1")).thenReturn(List.of(colheita));

        service.excluir(1L);

        var ordem = inOrder(produto, movimentacoes, plantacoes);
        ordem.verify(produto).baixarEstoque(new BigDecimal("10.00"));
        ordem.verify(movimentacoes).delete(colheita);
        ordem.verify(plantacoes).delete(plantacao);
    }
}
