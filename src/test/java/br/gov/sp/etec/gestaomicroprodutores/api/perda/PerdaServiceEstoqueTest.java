package br.gov.sp.etec.gestaomicroprodutores.api.perda;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.Cultura;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.dto.DadosCadastroPerda;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.Talhao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerdaServiceEstoqueTest {
    @Mock PerdaRepository perdas;
    @Mock PlantacaoRepository plantacoes;
    @Mock ProdutoRepository produtos;
    @Mock MovimentacaoEstoqueRepository movimentacoes;
    @InjectMocks PerdaService service;

    @Test
    void perdaNoCampoNaoRetiraEstoqueDeProdutoNaoColhido() {
        Plantacao plantacao = plantacao("EM_ANDAMENTO");
        when(plantacoes.findById(1L)).thenReturn(Optional.of(plantacao));
        when(perdas.save(any(Perda.class))).thenAnswer(i -> i.getArgument(0));
        var dados = new DadosCadastroPerda(1L, LocalDate.of(2026,10,18),
                new BigDecimal("5"), "Pragas", BigDecimal.ZERO, null);
        var retorno = service.cadastrar(dados);
        assertEquals("EM_CAMPO", retorno.etapa());
        verifyNoInteractions(produtos, movimentacoes);
    }

    @Test
    void perdaAposColheitaBaixaDoProdutoVinculado() {
        Plantacao plantacao = plantacao("COLHIDA");
        Produto produto = mock(Produto.class);
        when(plantacao.getProdutoColheita()).thenReturn(produto);
        when(produto.getIdProduto()).thenReturn(7L);
        when(produtos.buscarParaMovimentacao(7L)).thenReturn(Optional.of(produto));
        when(produto.getQuantidadeEstoque()).thenReturn(new BigDecimal("18"));
        when(plantacao.getDataColheita()).thenReturn(LocalDate.of(2026,10,17));
        when(plantacoes.findById(1L)).thenReturn(Optional.of(plantacao));
        when(perdas.save(any(Perda.class))).thenAnswer(i -> i.getArgument(0));
        var dados = new DadosCadastroPerda(1L, LocalDate.of(2026,10,18),
                new BigDecimal("4"), "Estragou no armazenamento", BigDecimal.ZERO, null);
        var retorno = service.cadastrar(dados);
        assertEquals("POS_COLHEITA", retorno.etapa());
        verify(produto).baixarEstoque(new BigDecimal("4"));
        verify(movimentacoes).save(any());
    }

    private Plantacao plantacao(String status) {
        Plantacao pl = mock(Plantacao.class);
        Talhao talhao = mock(Talhao.class);
        Cultura cultura = mock(Cultura.class);
        when(pl.getStatus()).thenReturn(status);
        when(pl.getDataPlantio()).thenReturn(LocalDate.of(2026,10,1));
        when(pl.getTalhao()).thenReturn(talhao);
        when(pl.getCultura()).thenReturn(cultura);
        return pl;
    }
}
