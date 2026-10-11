package br.gov.sp.etec.gestaomicroprodutores.api.gasto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroGasto(
        @NotNull(message = "O ID da plantação é obrigatório")
        Long idPlantacao,

        @NotBlank(message = "A descrição do gasto é obrigatória")
        @Size(max = 150, message = "A descrição deve ter no máximo 150 caracteres")
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A data do gasto é obrigatória")
        LocalDate dataGasto,

        @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
        String categoria,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
