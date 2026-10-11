package br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroPlantacao(
        @NotNull(message = "O ID do talhão é obrigatório")
        Long idTalhao,

        @NotNull(message = "O ID da cultura é obrigatório")
        Long idCultura,

        @NotNull(message = "A data de plantio é obrigatória")
        LocalDate dataPlantio,

        LocalDate dataColheita,

        @NotNull(message = "A quantidade plantada é obrigatória")
        @Positive(message = "A quantidade plantada deve ser maior que zero")
        BigDecimal quantidadePlantada,

        BigDecimal quantidadeColhida,

        @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
        String status,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
