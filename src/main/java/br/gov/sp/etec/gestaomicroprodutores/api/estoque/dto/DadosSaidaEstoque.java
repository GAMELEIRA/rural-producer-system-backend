package br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DadosSaidaEstoque(
        @NotNull(message = "O ID do produto é obrigatório")
        Long idProduto,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        BigDecimal quantidade,

        @Size(max = 30, message = "O tipo deve ter no máximo 30 caracteres")
        String tipo, // SAIDA_DESCARTE, SAIDA_AVULSA, AJUSTE

        @Size(max = 100, message = "O motivo deve ter no máximo 100 caracteres")
        String motivo,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
