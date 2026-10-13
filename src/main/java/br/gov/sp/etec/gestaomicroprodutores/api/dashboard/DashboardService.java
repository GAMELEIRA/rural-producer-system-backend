package br.gov.sp.etec.gestaomicroprodutores.api.dashboard;

import br.gov.sp.etec.gestaomicroprodutores.api.aplicacaoinsumo.AplicacaoInsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.cultura.CulturaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.dashboard.dto.*;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducao;
import br.gov.sp.etec.gestaomicroprodutores.api.gasto.GastoProducaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.insumo.InsumoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.perda.PerdaRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.PlantacaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.Produto;
import br.gov.sp.etec.gestaomicroprodutores.api.produto.ProdutoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.SafraRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.talhao.TalhaoRepository;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.Venda;
import br.gov.sp.etec.gestaomicroprodutores.api.venda.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private CulturaRepository culturaRepository;

    @Autowired
    private PlantacaoRepository plantacaoRepository;

    @Autowired
    private SafraRepository safraRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private AplicacaoInsumoRepository aplicacaoInsumoRepository;

    @Autowired
    private GastoProducaoRepository gastoProducaoRepository;

    @Autowired
    private PerdaRepository perdaRepository;

    public DadosResumoDashboard obterResumo() {
        Long totalTalhoes = talhaoRepository.count();
        Long totalCulturas = culturaRepository.count();
        Long totalPlantacoesAtivas = plantacaoRepository.countByStatusNotIgnoreCase("COLHIDA");
        Long totalSafrasEmAndamento = safraRepository.countByStatusIgnoreCase("EM_ANDAMENTO");
        Long totalProdutosCadastrados = produtoRepository.count();

        BigDecimal valorEstoqueProdutos = produtoRepository.calcularValorTotalEstoqueProdutos();
        BigDecimal valorEstoqueInsumos = insumoRepository.calcularValorTotalEstoqueInsumos();

        BigDecimal receitaVendas = vendaRepository.somarTotalVendasConcluidas();
        Long totalVendas = vendaRepository.contarVendasConcluidas();

        BigDecimal custoInsumos = aplicacaoInsumoRepository.somarTotalCustos();
        BigDecimal custoGastos = gastoProducaoRepository.somarTotalGastos();
        BigDecimal custoPerdas = perdaRepository.somarTotalPerdasEstimadas();

        BigDecimal despesasTotais = custoInsumos.add(custoGastos);
        BigDecimal saldoLiquido = receitaVendas.subtract(despesasTotais);

        BigDecimal margemLucro = BigDecimal.ZERO;
        if (receitaVendas.compareTo(BigDecimal.ZERO) > 0) {
            margemLucro = saldoLiquido
                    .multiply(BigDecimal.valueOf(100))
                    .divide(receitaVendas, 2, RoundingMode.HALF_UP);
        }

        return new DadosResumoDashboard(
                totalTalhoes,
                totalCulturas,
                totalPlantacoesAtivas,
                totalSafrasEmAndamento,
                totalProdutosCadastrados,
                valorEstoqueProdutos,
                valorEstoqueInsumos,
                receitaVendas,
                totalVendas,
                custoInsumos,
                custoGastos,
                custoPerdas,
                despesasTotais,
                saldoLiquido,
                margemLucro
        );
    }

    public DadosGraficosDashboard obterGraficos() {
        // 1. Vendas por Mês (agrupadas por MM/yyyy)
        List<Venda> vendas = vendaRepository.findByStatusOrderByDataVendaDesc("CONCLUIDA");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");
        Map<String, BigDecimal> vendasPorMesMap = new LinkedHashMap<>();
        for (Venda v : vendas) {
            String mesAno = v.getDataVenda().format(fmt);
            vendasPorMesMap.merge(mesAno, v.getValorTotal(), BigDecimal::add);
        }
        List<ItemGraficoMes> vendasPorMes = vendasPorMesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(s -> java.time.YearMonth.parse(s, fmt))))
                .map(e -> new ItemGraficoMes(e.getKey(), e.getValue()))
                .toList();

        // 2. Despesas por Categoria
        Map<String, BigDecimal> despesasMap = new LinkedHashMap<>();
        BigDecimal insumosTotal = aplicacaoInsumoRepository.somarTotalCustos();
        if (insumosTotal != null && insumosTotal.compareTo(BigDecimal.ZERO) > 0) {
            despesasMap.put("INSUMOS_AGRICOLAS", insumosTotal);
        }
        List<GastoProducao> gastos = gastoProducaoRepository.findAll();
        for (GastoProducao g : gastos) {
            String cat = g.getCategoria() != null ? g.getCategoria() : "OUTROS";
            despesasMap.merge(cat, g.getValor(), BigDecimal::add);
        }
        List<ItemGraficoCategoria> despesasPorCategoria = despesasMap.entrySet().stream()
                .map(e -> new ItemGraficoCategoria(e.getKey(), e.getValue()))
                .toList();

        // 3. Status das Plantações
        List<Plantacao> plantacoes = plantacaoRepository.findAll();
        Map<String, Long> statusMap = plantacoes.stream()
                .collect(Collectors.groupingBy(p -> p.getStatus() != null ? p.getStatus() : "INDEFINIDO", Collectors.counting()));
        List<ItemGraficoStatus> statusPlantacoes = statusMap.entrySet().stream()
                .map(e -> new ItemGraficoStatus(e.getKey(), e.getValue()))
                .toList();

        // 4. Estoque de Produtos
        List<Produto> produtos = produtoRepository.findAll();
        List<ItemGraficoProduto> estoqueProdutos = produtos.stream()
                .map(p -> new ItemGraficoProduto(
                        p.getIdProduto(),
                        p.getNome(),
                        p.getUnidadeMedida(),
                        p.getQuantidadeEstoque(),
                        p.getPrecoVenda(),
                        p.getQuantidadeEstoque().multiply(p.getPrecoVenda())
                ))
                .toList();

        return new DadosGraficosDashboard(
                vendasPorMes,
                despesasPorCategoria,
                statusPlantacoes,
                estoqueProdutos
        );
    }
}
