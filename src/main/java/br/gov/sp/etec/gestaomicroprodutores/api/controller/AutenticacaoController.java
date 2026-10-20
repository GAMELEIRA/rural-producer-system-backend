package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.security.TokenService;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.Login;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.UsuarioService;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosAutenticacao;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosCadastroUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosDetalhamentoUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosTokenJWT;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados) {
        log.info("Login solicitado para e-mail={}", dados.email());
        var tokenAutenticacao = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(tokenAutenticacao);

        var login = (Login) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(login);
        log.info("Login bem-sucedido: idUsuario={} e-mail={}", login.getUsuario().getIdUsuario(), login.getEmail());

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
        log.info("Cadastro solicitado: nome={} sobrenome={} e-mail={} cpf={} telefone={}",
                dados.nome(), dados.sobrenome(), dados.email(), dados.cpf(), dados.telefone());
        var usuarioDetalhado = usuarioService.cadastrar(dados);
        log.info("Usuario cadastrado: idUsuario={} e-mail={}", usuarioDetalhado.idUsuario(), usuarioDetalhado.email());
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioDetalhado.idUsuario()).toUri();

        return ResponseEntity.created(uri).body(usuarioDetalhado);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<Void> recuperarSenha(@RequestBody @Valid br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosRecuperacaoSenha dados) {
        log.info("Recuperação de senha solicitada para e-mail={}", dados.email());
        return ResponseEntity.ok().build();
    }
}
