package br.gov.sp.etec.gestaomicroprodutores.api.plantacao.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizacaoPlantacao(
        Long idTalhao,
        Long idCultura,
        LocalDate dataPlantio,
        LocalDate dataColheita,

        @Positive(message = "A quantidade plantada deve ser maior que zero")
        BigDecimal quantidadePlantada,

        BigDecimal quantidadeColhida,

        @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
        String status,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
