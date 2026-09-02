package br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto;

import jakarta.validation.constraints.Size;

public record DadosAtualizacaoUsuario(
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 100, message = "O sobrenome deve ter no máximo 100 caracteres")
        String sobrenome,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone
) {
}
