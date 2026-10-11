package br.gov.sp.etec.gestaomicroprodutores.api.controller;

import br.gov.sp.etec.gestaomicroprodutores.api.usuario.Login;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.UsuarioService;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosAtualizacaoUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosDetalhamentoUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/usuarios", "/usuarios/"})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<DadosDetalhamentoUsuario> obterPerfilAtual(@AuthenticationPrincipal Login login) {
        var usuario = usuarioService.buscarPorId(login.getUsuario().getIdUsuario());
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/me")
    public ResponseEntity<DadosDetalhamentoUsuario> atualizarPerfil(
            @AuthenticationPrincipal Login login,
            @RequestBody @Valid DadosAtualizacaoUsuario dados
    ) {
        var usuarioAtualizado = usuarioService.atualizar(login.getUsuario().getIdUsuario(), dados);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> desativarConta(@AuthenticationPrincipal Login login) {
        usuarioService.desativar(login.getUsuario().getIdUsuario());
        return ResponseEntity.noContent().build();
    }
}
