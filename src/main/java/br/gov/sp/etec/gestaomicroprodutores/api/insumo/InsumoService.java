package br.gov.sp.etec.gestaomicroprodutores.api.insumo;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.validation.ImagemCadastroValidator;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoInsumoRepository;
import java.math.BigDecimal;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto.DadosCadastroInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.dto.DadosDetalhamentoInsumo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private MovimentacaoInsumoRepository movimentacaoInsumoRepository;

    public List<DadosDetalhamentoInsumo> listar() {
        return insumoRepository.findAll().stream().map(DadosDetalhamentoInsumo::new).toList();
    }

    public DadosDetalhamentoInsumo buscarPorId(Long id) {
        return new DadosDetalhamentoInsumo(obter(id));
    }

    @Transactional
    public DadosDetalhamentoInsumo cadastrar(DadosCadastroInsumo dados) {
        if (insumoRepository.existsByNomeIgnoreCase(dados.nome())) {
            throw new RegraDeNegocioException("Ja existe umo insumo cadastrado com este nome!");
        }
        ImagemCadastroValidator.validar(dados.imagemPersonalizada());
        Insumo insumo = insumoRepository.save(new Insumo(dados));
        if (insumo.getQuantidadeEstoque().signum() > 0) {
            movimentacaoInsumoRepository.save(new MovimentacaoInsumo(insumo, "ENTRADA_INICIAL",
                    insumo.getQuantidadeEstoque(), "Estoque inicial cadastrado"));
        }
        return new DadosDetalhamentoInsumo(insumo);
    }

    @Transactional
    public DadosDetalhamentoInsumo atualizar(Long id, DadosCadastroInsumo dados) {
        Insumo insumo = obter(id);
        if (insumoRepository.existsByNomeIgnoreCaseAndIdInsumoNot(dados.nome(), id)) {
            throw new RegraDeNegocioException("Ja existe umo insumo cadastrado com este nome!");
        }
        ImagemCadastroValidator.validar(dados.imagemPersonalizada());
        BigDecimal anterior = insumo.getQuantidadeEstoque();
        insumo.atualizarInformacoes(dados);
        BigDecimal diferenca = insumo.getQuantidadeEstoque().subtract(anterior);
        if (diferenca.signum() != 0) {
            movimentacaoInsumoRepository.save(new MovimentacaoInsumo(insumo,
                    diferenca.signum() > 0 ? "AJUSTE_ENTRADA" : "AJUSTE_SAIDA",
                    diferenca.abs(), "Correção manual do saldo no cadastro"));
        }
        return new DadosDetalhamentoInsumo(insumo);
    }

    @Transactional
    public void excluir(Long id) {
        insumoRepository.delete(obter(id));
    }

    private Insumo obter(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insumo nao encontrado!"));
    }
}
