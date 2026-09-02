package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.security.TokenService;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.Login;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.UsuarioService;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosAutenticacao;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosCadastroUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosDetalhamentoUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosTokenJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados) {
        var tokenAutenticacao = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(tokenAutenticacao);

        var login = (Login) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(login);

        return ResponseEntity.ok(new DadosTokenJWT(
                tokenJWT,
                login.getUsuario().getIdUsuario(),
                login.getUsuario().getNome(),
                login.getEmail()
        ));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder
    ) {
        var usuarioDetalhado = usuarioService.cadastrar(dados);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioDetalhado.idUsuario()).toUri();

        return ResponseEntity.created(uri).body(usuarioDetalhado);
    }
}
