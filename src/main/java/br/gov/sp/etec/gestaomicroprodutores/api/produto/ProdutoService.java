package br.gov.sp.etec.gestaomicroprodutores.api.produto;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.dto.DadosCadastroProduto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.dto.DadosDetalhamentoProduto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<DadosDetalhamentoProduto> listar() {
        return produtoRepository.findAll().stream().map(DadosDetalhamentoProduto::new).toList();
    }

    public DadosDetalhamentoProduto buscarPorId(Long id) {
        return new DadosDetalhamentoProduto(obter(id));
    }

    @Transactional
    public DadosDetalhamentoProduto cadastrar(DadosCadastroProduto dados) {
        if (produtoRepository.existsByNomeIgnoreCase(dados.nome())) {
            throw new RegraDeNegocioException("Ja existe umo produto cadastrado com este nome!");
        }
        Produto produto = produtoRepository.save(new Produto(dados));
        return new DadosDetalhamentoProduto(produto);
    }

    @Transactional
    public DadosDetalhamentoProduto atualizar(Long id, DadosCadastroProduto dados) {
        Produto produto = obter(id);
        if (produtoRepository.existsByNomeIgnoreCaseAndIdProdutoNot(dados.nome(), id)) {
            throw new RegraDeNegocioException("Ja existe umo produto cadastrado com este nome!");
        }
        produto.atualizarDadosCadastrais(dados);
        return new DadosDetalhamentoProduto(produto);
    }

    @Transactional
    public void excluir(Long id) {
        produtoRepository.delete(obter(id));
    }

    private Produto obter(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto nao encontrado!"));
    }
}
