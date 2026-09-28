package br.gov.sp.etec.gestaomicroprodutores.api.infra.security;

import br.gov.sp.etec.gestaomicroprodutores.api.usuario.LoginRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LoginRepository loginRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject(tokenJWT);
                var login = loginRepository.findByEmail(subject);

                if (login != null && login.isEnabled()) {
                    var authentication = new UsernamePasswordAuthenticationToken(login, null, login.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Token JWT valido para e-mail={}", subject);
                } else {
                    log.warn("Token JWT valido, mas usuario inexistente ou inativo: e-mail={}", subject);
                }
            } catch (Exception e) {
                // Token invalido/expirado: a requisicao segue anonima e o Spring Security rejeita se a rota for restrita
                log.warn("Token JWT rejeitado em {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}
