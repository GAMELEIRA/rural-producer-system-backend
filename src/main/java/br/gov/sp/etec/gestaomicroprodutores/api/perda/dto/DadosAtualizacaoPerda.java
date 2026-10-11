package br.gov.sp.etec.gestaomicroprodutores.api.perda.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizacaoPerda(
        LocalDate dataPerda,

        @Positive(message = "A quantidade perdida deve ser maior que zero")
        BigDecimal quantidade,

        @Size(max = 150, message = "O motivo deve ter no máximo 150 caracteres")
        String motivo,

        @PositiveOrZero(message = "O valor estimado não pode ser negativo")
        BigDecimal valorEstimado,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
