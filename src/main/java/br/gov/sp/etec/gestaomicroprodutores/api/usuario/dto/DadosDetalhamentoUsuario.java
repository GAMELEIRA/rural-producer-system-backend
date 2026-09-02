package br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto;

import br.gov.sp.etec.gestaomicroprodutores.api.usuario.Usuario;

import java.time.LocalDateTime;

public record DadosDetalhamentoUsuario(
        Long idUsuario,
        String nome,
        String sobrenome,
        String email,
        String cpf,
        String telefone,
        LocalDateTime dataCadastro,
        boolean ativo
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTelefone(),
                usuario.getDataCadastro(),
                usuario.isAtivo()
        );
    }
}
