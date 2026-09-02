package br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Size(max = 100, message = "O sobrenome deve ter no máximo 100 caracteres")
        String sobrenome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "(^\\d{11}$)|(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)", message = "O CPF deve ter 11 dígitos ou o formato 000.000.000-00")
        String cpf,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha
) {
}
