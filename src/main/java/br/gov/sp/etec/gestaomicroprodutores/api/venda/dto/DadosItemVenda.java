package br.gov.sp.etec.gestaomicroprodutores.api.venda.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DadosItemVenda(
        @NotNull(message = "O ID do produto é obrigatório")
        Long idProduto,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        BigDecimal quantidade,

        @NotNull(message = "O preço unitário é obrigatório")
        @Positive(message = "O preço unitário deve ser maior que zero")
        BigDecimal precoUnitario
) {
}
