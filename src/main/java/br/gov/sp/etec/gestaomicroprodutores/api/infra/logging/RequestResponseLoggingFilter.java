package br.gov.sp.etec.gestaomicroprodutores.api.infra.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Registra em log cada requisicao HTTP recebida (metodo, URI, origem, headers relevantes e body)
 * e a resposta enviada (status, tempo e body). Campos sensiveis (senha/token) sao mascarados.
 *
 * Executa antes do Spring Security para que rotas bloqueadas (401/403) e o preflight CORS
 * (OPTIONS) tambem aparecam no log.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    private static final int MAX_BODY_LENGTH = 2000;
    private static final Pattern SENSITIVE_FIELDS =
            Pattern.compile("(\"(senha|password|token)\"\\s*:\\s*\")[^\"]*(\")", Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var requestId = UUID.randomUUID().toString().substring(0, 8);
        var wrappedRequest = new ContentCachingRequestWrapper(request);
        var wrappedResponse = new ContentCachingResponseWrapper(response);
        long inicio = System.currentTimeMillis();

        log.info("[{}] --> {} {}{} | Origin={} | Content-Type={} | Authorization={}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() != null ? "?" + request.getQueryString() : "",
                request.getHeader("Origin"),
                request.getContentType(),
                request.getHeader("Authorization") != null ? "Bearer ***" : null);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duracao = System.currentTimeMillis() - inicio;
            var requestBody = mascarar(toString(wrappedRequest.getContentAsByteArray()));
            var responseBody = mascarar(toString(wrappedResponse.getContentAsByteArray()));

            if (!requestBody.isBlank()) {
                log.info("[{}]     request body: {}", requestId, requestBody);
            }

            int status = wrappedResponse.getStatus();
            String linha = "[{}] <-- {} {} | status={} | {}ms | response body: {}";
            if (status >= 500) {
                log.error(linha, requestId, request.getMethod(), request.getRequestURI(), status, duracao, responseBody);
            } else if (status >= 400) {
                log.warn(linha, requestId, request.getMethod(), request.getRequestURI(), status, duracao, responseBody);
            } else {
                log.info(linha, requestId, request.getMethod(), request.getRequestURI(), status, duracao, responseBody);
            }

            wrappedResponse.copyBodyToResponse();
        }
    }

    private static String toString(byte[] content) {
        if (content == null || content.length == 0) return "";
        var texto = new String(content, StandardCharsets.UTF_8);
        return texto.length() > MAX_BODY_LENGTH ? texto.substring(0, MAX_BODY_LENGTH) + "...(truncado)" : texto;
    }

    private static String mascarar(String body) {
        return SENSITIVE_FIELDS.matcher(body).replaceAll("$1***$3");
    }
}
