package br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DadosCadastroTalhao(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "A área é obrigatória")
        @Positive(message = "A área deve ser maior que zero")
        BigDecimal area,

        @NotBlank(message = "A unidade da área é obrigatória")
        @Size(max = 20, message = "A unidade da área deve ter no máximo 20 caracteres")
        String unidadeArea,

        @NotBlank(message = "A localização é obrigatória")
        @Size(max = 200, message = "A localização deve ter no máximo 200 caracteres")
        String localizacao,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
