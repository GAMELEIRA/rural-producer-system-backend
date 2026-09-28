package br.gov.sp.etec.gestaomicroprodutores.api.cultura;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto.DadosCadastroCultura;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto.DadosDetalhamentoCultura;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CulturaService {

    @Autowired
    private CulturaRepository culturaRepository;

    public List<DadosDetalhamentoCultura> listar() {
        return culturaRepository.findAll().stream().map(DadosDetalhamentoCultura::new).toList();
    }

    public DadosDetalhamentoCultura buscarPorId(Long id) {
        return new DadosDetalhamentoCultura(obter(id));
    }

    @Transactional
    public DadosDetalhamentoCultura cadastrar(DadosCadastroCultura dados) {
        if (culturaRepository.existsByNomeIgnoreCase(dados.nome())) {
            throw new RegraDeNegocioException("Ja existe uma cultura cadastrada com este nome!");
        }
        Cultura cultura = culturaRepository.save(new Cultura(dados));
        return new DadosDetalhamentoCultura(cultura);
    }

    @Transactional
    public DadosDetalhamentoCultura atualizar(Long id, DadosCadastroCultura dados) {
        Cultura cultura = obter(id);
        if (culturaRepository.existsByNomeIgnoreCaseAndIdCulturaNot(dados.nome(), id)) {
            throw new RegraDeNegocioException("Ja existe uma cultura cadastrada com este nome!");
        }
        cultura.atualizarInformacoes(dados);
        return new DadosDetalhamentoCultura(cultura);
    }

    @Transactional
    public void excluir(Long id) {
        culturaRepository.delete(obter(id));
    }

    private Cultura obter(Long id) {
        return culturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cultura nao encontrada!"));
    }
}
