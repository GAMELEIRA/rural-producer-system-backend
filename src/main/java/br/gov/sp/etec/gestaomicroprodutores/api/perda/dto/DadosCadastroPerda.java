package br.gov.sp.etec.gestaomicroprodutores.api.perda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroPerda(
        @NotNull(message = "O ID da plantação é obrigatório")
        Long idPlantacao,

        @NotNull(message = "A data da perda é obrigatória")
        LocalDate dataPerda,

        @NotNull(message = "A quantidade perdida é obrigatória")
        @Positive(message = "A quantidade perdida deve ser maior que zero")
        BigDecimal quantidade,

        @NotBlank(message = "O motivo da perda é obrigatório")
        @Size(max = 150, message = "O motivo deve ter no máximo 150 caracteres")
        String motivo,

        @PositiveOrZero(message = "O valor estimado não pode ser negativo")
        BigDecimal valorEstimado,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
