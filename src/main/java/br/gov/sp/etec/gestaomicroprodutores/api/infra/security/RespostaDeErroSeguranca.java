package br.gov.sp.etec.gestaomicroprodutores.api.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Converte bloqueios do Spring Security em respostas JSON no mesmo formato do TratadorDeErros
 * ({@code {"mensagem": ...}}) e registra o motivo em log.
 *
 * Sem isto, uma rota inexistente ou protegida acessada sem token devolve 403 sem corpo,
 * o que dificulta o diagnostico no frontend.
 */
@Component
public class RespostaDeErroSeguranca implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(RespostaDeErroSeguranca.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        log.warn("Acesso nao autenticado bloqueado: {} {} ({})", request.getMethod(), request.getRequestURI(), ex.getMessage());
        escrever(response, HttpStatus.UNAUTHORIZED,
                "Autenticacao necessaria para acessar " + request.getMethod() + " " + request.getRequestURI()
                        + ". Verifique se a rota existe e se o token JWT foi enviado.");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException {
        log.warn("Acesso negado: {} {} ({})", request.getMethod(), request.getRequestURI(), ex.getMessage());
        escrever(response, HttpStatus.FORBIDDEN, "Voce nao tem permissao para acessar este recurso.");
    }

    private void escrever(HttpServletResponse response, HttpStatus status, String mensagem) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"mensagem\":\"" + mensagem.replace("\"", "'") + "\"}");
    }
}
