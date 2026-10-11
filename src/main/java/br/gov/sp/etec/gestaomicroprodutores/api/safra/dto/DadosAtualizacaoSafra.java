package br.gov.sp.etec.gestaomicroprodutores.api.safra.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DadosAtualizacaoSafra(
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        LocalDate dataInicio,
        LocalDate dataFim,

        @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
        String status,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
