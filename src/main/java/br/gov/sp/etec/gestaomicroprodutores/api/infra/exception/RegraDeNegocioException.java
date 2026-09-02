package br.gov.sp.etec.gestaomicroprodutores.api.infra.exception;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
