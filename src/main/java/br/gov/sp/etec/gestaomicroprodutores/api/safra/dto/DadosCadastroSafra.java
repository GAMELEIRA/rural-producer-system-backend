package br.gov.sp.etec.gestaomicroprodutores.api.safra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DadosCadastroSafra(
        @NotBlank(message = "O nome da safra é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "A data de início é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "A data de término é obrigatória")
        LocalDate dataFim,

        @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
        String status,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
