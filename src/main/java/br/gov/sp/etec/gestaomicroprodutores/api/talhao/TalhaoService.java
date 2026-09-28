package br.gov.sp.etec.gestaomicroprodutores.api.talhao;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto.DadosCadastroTalhao;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto.DadosDetalhamentoTalhao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TalhaoService {

    @Autowired
    private TalhaoRepository talhaoRepository;

    public List<DadosDetalhamentoTalhao> listar() {
        return talhaoRepository.findAll().stream().map(DadosDetalhamentoTalhao::new).toList();
    }

    public DadosDetalhamentoTalhao buscarPorId(Long id) {
        return new DadosDetalhamentoTalhao(obter(id));
    }

    @Transactional
    public DadosDetalhamentoTalhao cadastrar(DadosCadastroTalhao dados) {
        if (talhaoRepository.existsByNomeIgnoreCase(dados.nome())) {
            throw new RegraDeNegocioException("Ja existe umo talhão cadastrado com este nome!");
        }
        Talhao talhao = talhaoRepository.save(new Talhao(dados));
        return new DadosDetalhamentoTalhao(talhao);
    }

    @Transactional
    public DadosDetalhamentoTalhao atualizar(Long id, DadosCadastroTalhao dados) {
        Talhao talhao = obter(id);
        if (talhaoRepository.existsByNomeIgnoreCaseAndIdTalhaoNot(dados.nome(), id)) {
            throw new RegraDeNegocioException("Ja existe umo talhão cadastrado com este nome!");
        }
        talhao.atualizarInformacoes(dados);
        return new DadosDetalhamentoTalhao(talhao);
    }

    @Transactional
    public void excluir(Long id) {
        talhaoRepository.delete(obter(id));
    }

    private Talhao obter(Long id) {
        return talhaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Talhão nao encontrado!"));
    }
}
