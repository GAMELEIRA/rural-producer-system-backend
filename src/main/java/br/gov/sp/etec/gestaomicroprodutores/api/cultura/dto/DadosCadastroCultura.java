package br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DadosCadastroCultura(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O tipo é obrigatório")
        @Size(max = 50, message = "O tipo deve ter no máximo 50 caracteres")
        String tipo,

        @NotNull(message = "O ciclo estimado é obrigatório")
        @Positive(message = "O ciclo estimado deve ser maior que zero")
        Integer cicloEstimado,

        @NotBlank(message = "A unidade do ciclo é obrigatória")
        @Size(max = 20, message = "A unidade do ciclo deve ter no máximo 20 caracteres")
        String unidadeCiclo,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes,

        @Size(max = 40, message = "O código da imagem deve ter no máximo 40 caracteres")
        String imagemCodigo
) {
}
