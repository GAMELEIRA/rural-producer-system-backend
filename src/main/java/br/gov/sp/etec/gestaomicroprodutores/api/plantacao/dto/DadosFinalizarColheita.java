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

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
