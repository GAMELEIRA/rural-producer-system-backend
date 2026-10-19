package br.gov.sp.etec.gestaomicroprodutores.api.estoque;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto.DadosEntradaInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.Insumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.InsumoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoInsumoServiceTest {
    @Mock private InsumoRepository insumos;
    @Mock private MovimentacaoInsumoRepository movimentos;
    @InjectMocks private MovimentacaoInsumoService service;

    @Test
    void registraReposicaoNoEstoque() {
        Insumo insumo = mock(Insumo.class);
        when(insumos.buscarParaMovimentacao(3L)).thenReturn(Optional.of(insumo));
        when(movimentos.save(any(MovimentacaoInsumo.class))).thenAnswer(i -> i.getArgument(0));
        var resposta = service.registrarEntrada(new DadosEntradaInsumo(3L, new BigDecimal("5.00"), "Compra de sementes"));
        verify(insumo).estornarEstoque(new BigDecimal("5.00"));
        assertEquals("ENTRADA_REPOSICAO", resposta.tipo());
        assertEquals("Compra de sementes", resposta.origem());
    }

    @Test
    void registraSaidaNoEstoque() {
        Insumo insumo = mock(Insumo.class);
        when(insumos.buscarParaMovimentacao(3L)).thenReturn(Optional.of(insumo));
        when(movimentos.save(any(MovimentacaoInsumo.class))).thenAnswer(i -> i.getArgument(0));
        var resposta = service.registrarSaida(new DadosEntradaInsumo(3L, new BigDecimal("2.50"), null));
        verify(insumo).debitarEstoque(new BigDecimal("2.50"));
        assertEquals("SAIDA_AVULSA", resposta.tipo());
    }
}
