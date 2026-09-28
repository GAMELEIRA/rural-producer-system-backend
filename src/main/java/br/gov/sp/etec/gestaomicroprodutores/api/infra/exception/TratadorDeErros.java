package br.gov.sp.etec.gestaomicroprodutores.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger log = LoggerFactory.getLogger(TratadorDeErros.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> tratarErro404(EntityNotFoundException ex) {
        log.warn("Recurso nao encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage() != null ? ex.getMessage() : "Recurso nao encontrado"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400Validacao(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        log.warn("Erro de validacao no body: {}",
                erros.stream().map(e -> e.getField() + " -> " + e.getDefaultMessage()).toList());
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, String>> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        log.warn("Regra de negocio violada: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> tratarErroBadCredentials() {
        log.warn("Tentativa de login com credenciais invalidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "Credenciais invalidas (e-mail ou senha incorretos)"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> tratarErroAutenticacao(AuthenticationException ex) {
        log.warn("Falha na autenticacao: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "Falha na autenticacao: " + ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarBodyIlegivel(HttpMessageNotReadableException ex) {
        log.warn("Body da requisicao invalido ou ausente: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.badRequest().body(Map.of("mensagem", "Corpo da requisicao invalido ou ausente (JSON esperado)"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarErro500(Exception ex) {
        log.error("Erro interno nao tratado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensagem", "Erro interno do servidor: " + ex.getLocalizedMessage()));
    }

    public record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
