package br.gov.sp.etec.gestaomicroprodutores.api.financeiro;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.financeiro.dto.ResumoFinanceiroGeral;
import br.gov.sp.etec.gestaomicroprodutores.api.financeiro.dto.ResumoFinanceiroPlantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.PerdaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FinanceiroService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private AplicacaoInsumoRepository aplicacaoInsumoRepository;

    @Autowired
    private GastoProducaoRepository gastoProducaoRepository;

    @Autowired
    private PerdaRepository perdaRepository;

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    public ResumoFinanceiroGeral obterResumoGeral() {
        BigDecimal receitaTotalVendas = vendaRepository.somarTotalVendasConcluidas();
        Long totalVendasRealizadas = vendaRepository.contarVendasConcluidas();
        BigDecimal custoTotalInsumos = aplicacaoInsumoRepository.somarTotalCustos();
        BigDecimal custoTotalGastos = gastoProducaoRepository.somarTotalGastos();
        BigDecimal custoTotalPerdas = perdaRepository.somarTotalPerdasEstimadas();

        BigDecimal despesasTotais = custoTotalInsumos.add(custoTotalGastos);
        BigDecimal saldoLiquido = receitaTotalVendas.subtract(despesasTotais);

        BigDecimal margemLucro = BigDecimal.ZERO;
        if (receitaTotalVendas.compareTo(BigDecimal.ZERO) > 0) {
            margemLucro = saldoLiquido
                    .multiply(BigDecimal.valueOf(100))
                    .divide(receitaTotalVendas, 2, RoundingMode.HALF_UP);
        }

        return new ResumoFinanceiroGeral(
                receitaTotalVendas,
                totalVendasRealizadas,
                custoTotalInsumos,
                custoTotalGastos,
                custoTotalPerdas,
                despesasTotais,
                saldoLiquido,
                margemLucro
        );
    }

    public ResumoFinanceiroPlantacao obterResumoPlantacao(Long idPlantacao) {
        Plantacao plantacao = plantacaoRepository.findById(idPlantacao)
                .orElseThrow(() -> new EntityNotFoundException("Plantação não encontrada com ID: " + idPlantacao));

        BigDecimal custoInsumos = aplicacaoInsumoRepository.somarCustoTotalPorPlantacao(idPlantacao);
        BigDecimal gastosProducao = gastoProducaoRepository.somarGastosPorPlantacao(idPlantacao);
        BigDecimal perdasEstimadas = perdaRepository.somarValorEstimadoPorPlantacao(idPlantacao);

        BigDecimal custoTotalProducao = custoInsumos.add(gastosProducao);

        return new ResumoFinanceiroPlantacao(
                plantacao.getIdPlantacao(),
                plantacao.getCultura().getNome(),
                plantacao.getTalhao().getNome(),
                plantacao.getStatus(),
                custoInsumos,
                gastosProducao,
                perdasEstimadas,
                custoTotalProducao
        );
    }
}
