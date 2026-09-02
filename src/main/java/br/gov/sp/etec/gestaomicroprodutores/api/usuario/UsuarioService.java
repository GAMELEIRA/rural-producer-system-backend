package br.gov.sp.etec.gestaomicroprodutores.api.usuario;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosAtualizacaoUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosCadastroUsuario;
import br.gov.sp.etec.gestaomicroprodutores.api.usuario.dto.DadosDetalhamentoUsuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public DadosDetalhamentoUsuario cadastrar(DadosCadastroUsuario dados) {
        // Valida unicidade de e-mail e CPF
        if (usuarioRepository.existsByEmail(dados.email()) || loginRepository.existsByEmail(dados.email())) {
            throw new RegraDeNegocioException("Ja existe um usuario cadastrado com este e-mail!");
        }

        if (usuarioRepository.existsByCpf(dados.cpf())) {
            throw new RegraDeNegocioException("Ja existe um usuario cadastrado com este CPF!");
        }

        // 1. Cria e salva o Usuario
        Usuario usuario = new Usuario(
                dados.nome(),
                dados.sobrenome(),
                dados.email(),
                dados.cpf(),
                dados.telefone()
        );
        usuarioRepository.save(usuario);

        // 2. Criptografa a senha com BCrypt e cria o registro de Login associado
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        Login login = new Login(dados.email(), senhaCriptografada, usuario);
        loginRepository.save(login);

        return new DadosDetalhamentoUsuario(usuario);
    }

    public DadosDetalhamentoUsuario buscarPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado!"));
        return new DadosDetalhamentoUsuario(usuario);
    }

    public DadosDetalhamentoUsuario buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado para o e-mail informado!"));
        return new DadosDetalhamentoUsuario(usuario);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizar(Long idUsuario, DadosAtualizacaoUsuario dados) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado!"));

        usuario.atualizarInformacoes(dados.nome(), dados.sobrenome(), dados.telefone());
        return new DadosDetalhamentoUsuario(usuario);
    }

    @Transactional
    public void desativar(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado!"));
        usuario.desativar();
    }
}
