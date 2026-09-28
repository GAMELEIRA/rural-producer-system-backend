package br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DadosCadastroInsumo(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O tipo é obrigatório")
        @Size(max = 50, message = "O tipo deve ter no máximo 50 caracteres")
        String tipo,

        @NotBlank(message = "A unidade de medida é obrigatória")
        @Size(max = 20, message = "A unidade de medida deve ter no máximo 20 caracteres")
        String unidadeMedida,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa")
        BigDecimal quantidadeEstoque,

        @NotNull(message = "O preço unitário é obrigatório")
        @PositiveOrZero(message = "O preço unitário não pode ser negativo")
        BigDecimal precoUnitario,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
