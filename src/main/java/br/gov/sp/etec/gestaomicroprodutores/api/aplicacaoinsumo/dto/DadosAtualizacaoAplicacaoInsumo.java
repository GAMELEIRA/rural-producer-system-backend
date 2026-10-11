package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizacaoAplicacaoInsumo(
        LocalDate dataAplicacao,

        @Positive(message = "A quantidade aplicada deve ser maior que zero")
        BigDecimal quantidade,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
