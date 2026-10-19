package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.Cultura;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoque;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosFinalizarColheita;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.Talhao;
import org.junit.jupiter.api.BeforeEach;
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
class PlantacaoColheitaProdutoTest {
    @Mock PlantacaoRepository plantacoes;
    @Mock ProdutoRepository produtos;
    @Mock MovimentacaoEstoqueRepository movimentos;
    @InjectMocks PlantacaoService service;
    private Plantacao plantacao;
    private final LocalDate dataColheita = LocalDate.of(2026,10,19);

    @BeforeEach
    void preparar() {
        plantacao = mock(Plantacao.class);
        when(plantacoes.findById(1L)).thenReturn(Optional.of(plantacao));
        when(plantacao.getDataPlantio()).thenReturn(LocalDate.of(2026,10,1));
    }

    @Test
    void criaProdutoERegistraEntradaDeColheitaNaMesmaOperacao() {
        Cultura cultura = mock(Cultura.class);
        Talhao talhao = mock(Talhao.class);
        when(plantacao.getCultura()).thenReturn(cultura);
        when(cultura.getNome()).thenReturn("Cenoura");
        when(plantacao.getTalhao()).thenReturn(talhao);
        when(plantacao.getIdPlantacao()).thenReturn(1L);
        when(produtos.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        var dados = new DadosFinalizarColheita(dataColheita, new BigDecimal("7.0"),
                null, "kg", new BigDecimal("3.50"), null, null);
        service.finalizarColheita(1L, dados);

        verify(produtos).save(any(Produto.class));
        verify(movimentos).save(any(MovimentacaoEstoque.class));
        verify(plantacao).finalizarColheita(dados);
    }

    @Test
    void naoCriaProdutoSeDadosIncompletos() {
        var dados = new DadosFinalizarColheita(dataColheita, new BigDecimal("1.0"),
                null, null, null, null, null);
        assertThrows(RegraDeNegocioException.class, () -> service.finalizarColheita(1L, dados));
        verify(produtos, never()).save(any(Produto.class));
        verify(movimentos, never()).save(any(MovimentacaoEstoque.class));
    }
}
