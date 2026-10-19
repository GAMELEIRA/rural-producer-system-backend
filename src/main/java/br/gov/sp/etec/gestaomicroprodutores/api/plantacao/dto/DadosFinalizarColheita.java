package br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosFinalizarColheita(
        @NotNull(message = "A data da colheita é obrigatória")
        LocalDate dataColheita,

        @NotNull(message = "A quantidade colhida é obrigatória")
        @PositiveOrZero(message = "A quantidade colhida não pode ser negativa")
        BigDecimal quantidadeColhida,

        Long idProduto,

        @Size(max = 20, message = "Unidade do novo produto inválida")
        String unidadeNovoProduto,

        @PositiveOrZero(message = "O preço de venda não pode ser negativo")
        BigDecimal precoVendaNovoProduto,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes,

        @PositiveOrZero(message = "A perda na colheita não pode ser negativa")
        BigDecimal quantidadePerdidaColheita
) {
}
