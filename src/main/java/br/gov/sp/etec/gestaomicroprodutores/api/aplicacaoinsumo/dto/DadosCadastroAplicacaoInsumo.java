package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroAplicacaoInsumo(
        @NotNull(message = "O ID da plantação é obrigatório")
        Long idPlantacao,

        @NotNull(message = "O ID do insumo é obrigatório")
        Long idInsumo,

        @NotNull(message = "A data da aplicação é obrigatória")
        LocalDate dataAplicacao,

        @NotNull(message = "A quantidade aplicada é obrigatória")
        @Positive(message = "A quantidade aplicada deve ser maior que zero")
        BigDecimal quantidade,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
