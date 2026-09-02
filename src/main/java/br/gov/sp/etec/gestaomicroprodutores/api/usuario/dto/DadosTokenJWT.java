package br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto;

public record DadosTokenJWT(
        String token,
        String tipo,
        Long idUsuario,
        String nome,
        String email
) {
    public DadosTokenJWT(String token, Long idUsuario, String nome, String email) {
        this(token, "Bearer", idUsuario, nome, email);
    }
}
