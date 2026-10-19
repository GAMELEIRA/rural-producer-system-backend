package br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.DadosAtualizacaoAplicacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.estoque.MovimentacaoInsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.DadosCadastroAplicacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.DadosDetalhamentoAplicacaoInsumo;
import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.dto.ResumoCustoInsumosPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.Insumo;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.InsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AplicacaoInsumoService {

    @Autowired
    private AplicacaoInsumoRepository aplicacaoInsumoRepository;

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private MovimentacaoInsumoRepository movimentacaoInsumoRepository;

    public List<DadosDetalhamentoAplicacaoInsumo> listar() {
        return aplicacaoInsumoRepository.findAllComRelacionamentos()
                .stream()
                .map(DadosDetalhamentoAplicacaoInsumo::new)
                .toList();
    }

    public DadosDetalhamentoAplicacaoInsumo buscarPorId(Long id) {
        return new DadosDetalhamentoAplicacaoInsumo(obter(id));
    }

    public List<DadosDetalhamentoAplicacaoInsumo> listarPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao)) {
            throw new EntityNotFoundException("Plantação não encontrada com ID: " + idPlantacao);
        }
        return aplicacaoInsumoRepository.findByPlantacaoIdPlantacao(idPlantacao)
                .stream()
                .map(DadosDetalhamentoAplicacaoInsumo::new)
                .toList();
    }

    public ResumoCustoInsumosPlantacao obterResumoCustoPorPlantacao(Long idPlantacao) {
        if (!plantacaoRepository.existsById(idPlantacao)) {
            throw new EntityNotFoundException("Plantação não encontrada com ID: " + idPlantacao);
        }
        var aplicacoes = aplicacaoInsumoRepository.findByPlantacaoIdPlantacao(idPlantacao);
        BigDecimal totalCusto = aplicacoes.stream()
                .map(AplicacaoInsumo::getCustoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumoCustoInsumosPlantacao(idPlantacao, (long) aplicacoes.size(), totalCusto);
    }

    @Transactional
    public DadosDetalhamentoAplicacaoInsumo cadastrar(DadosCadastroAplicacaoInsumo dados) {
        Plantacao plantacao = plantacaoRepository.findById(dados.idPlantacao())
                .orElseThrow(() -> new EntityNotFoundException("Plantação não encontrada com ID: " + dados.idPlantacao()));

        Insumo insumo = insumoRepository.buscarParaMovimentacao(dados.idInsumo())
                .orElseThrow(() -> new EntityNotFoundException("Insumo não encontrado com ID: " + dados.idInsumo()));

        if (dados.dataAplicacao().isBefore(plantacao.getDataPlantio())) {
            throw new RegraDeNegocioException("A data da aplicação não pode ser anterior à data de plantio (" + plantacao.getDataPlantio() + ")!");
        }

        // Abate automático do estoque do insumo
        insumo.debitarEstoque(dados.quantidade());

        // Cálculo consistente do custo total com base no preço unitário do insumo
        BigDecimal custoTotal = dados.quantidade()
                .multiply(insumo.getPrecoUnitario())
                .setScale(2, RoundingMode.HALF_UP);

        AplicacaoInsumo aplicacao = new AplicacaoInsumo(
                plantacao,
                insumo,
                dados.dataAplicacao(),
                dados.quantidade(),
                custoTotal,
                dados.observacoes()
        );

        aplicacaoInsumoRepository.save(aplicacao);
        movimentacaoInsumoRepository.save(new MovimentacaoInsumo(insumo, "SAIDA_APLICACAO",
                dados.quantidade(), "Aplicação na plantação #" + plantacao.getIdPlantacao()));

        return new DadosDetalhamentoAplicacaoInsumo(aplicacao);
    }

    @Transactional
    public DadosDetalhamentoAplicacaoInsumo atualizar(Long id, DadosAtualizacaoAplicacaoInsumo dados) {
        AplicacaoInsumo aplicacao = obter(id);
        Insumo insumo = aplicacao.getInsumo();

        BigDecimal novaQuantidade = dados.quantidade() != null ? dados.quantidade() : aplicacao.getQuantidade();
        BigDecimal novoCustoTotal = aplicacao.getCustoTotal();

        // Se a quantidade foi alterada, recalcula estoque e custo
        if (dados.quantidade() != null && !dados.quantidade().equals(aplicacao.getQuantidade())) {
            // Estorna a quantidade anterior
            insumo.estornarEstoque(aplicacao.getQuantidade());
            movimentacaoInsumoRepository.save(new MovimentacaoInsumo(insumo, "ENTRADA_ESTORNO",
                    aplicacao.getQuantidade(), "Estorno da aplicação #" + id));
            // Debita a nova quantidade
            insumo.debitarEstoque(dados.quantidade());
            movimentacaoInsumoRepository.save(new MovimentacaoInsumo(insumo, "SAIDA_APLICACAO",
                    dados.quantidade(), "Alteração da aplicação #" + id));

            novoCustoTotal = dados.quantidade()
                    .multiply(insumo.getPrecoUnitario())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (dados.dataAplicacao() != null && dados.dataAplicacao().isBefore(aplicacao.getPlantacao().getDataPlantio())) {
            throw new RegraDeNegocioException("A data da aplicação não pode ser anterior à data de plantio (" + aplicacao.getPlantacao().getDataPlantio() + ")!");
        }

        aplicacao.atualizarInformacoes(dados.dataAplicacao(), novaQuantidade, novoCustoTotal, dados.observacoes());

        return new DadosDetalhamentoAplicacaoInsumo(aplicacao);
    }

    @Transactional
    public void excluir(Long id) {
        AplicacaoInsumo aplicacao = obter(id);

        // Estorna a quantidade utilizada de volta para o estoque do insumo
        aplicacao.getInsumo().estornarEstoque(aplicacao.getQuantidade());
        movimentacaoInsumoRepository.save(new MovimentacaoInsumo(aplicacao.getInsumo(),
                "ENTRADA_ESTORNO", aplicacao.getQuantidade(), "Exclusão da aplicação #" + id));

        aplicacaoInsumoRepository.delete(aplicacao);
    }

    private AplicacaoInsumo obter(Long id) {
        return aplicacaoInsumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aplicação de insumo não encontrada com ID: " + id));
    }
}
