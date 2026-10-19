package br.gov.sp.etec.gestaomicroprodutores.api.plantacao;

import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoEstoqueRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto.DadosFinalizarColheita;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
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
class PlantacaoServiceTest {
    @Mock PlantacaoRepository plantacaoRepository;
    @Mock ProdutoRepository produtoRepository;
    @Mock MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    @InjectMocks PlantacaoService service;

    @Test
    void naoFinalizaUmaPlantacaoJaColhida() {
        Plantacao plantacao = mock(Plantacao.class);
        when(plantacao.getStatus()).thenReturn("COLHIDA");
        when(plantacaoRepository.findById(1L)).thenReturn(Optional.of(plantacao));

        DadosFinalizarColheita dados = new DadosFinalizarColheita(
                LocalDate.of(2026, 10, 13), BigDecimal.TEN, 2L, null, null, null);

        assertThrows(RegraDeNegocioException.class,
                () -> service.finalizarColheita(1L, dados));
        verifyNoInteractions(produtoRepository, movimentacaoEstoqueRepository);
    }
}
