package br.gov.sp.etec.gestaomicroprodutores.api.venda;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Venda")
@Table(name = "tb_venda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "idVenda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    private Long idVenda;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;

    @Column(length = 150)
    private String cliente;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "forma_pagamento", length = 50)
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private String status = "CONCLUIDA";

    @Column(length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda(LocalDateTime dataVenda, String cliente, String formaPagamento, String observacoes) {
        this.dataVenda = dataVenda != null ? dataVenda : LocalDateTime.now();
        this.cliente = cliente != null ? cliente.trim() : "Consumidor Final";
        this.formaPagamento = formaPagamento != null ? formaPagamento.trim().toUpperCase() : "DINHEIRO";
        this.status = "CONCLUIDA";
        this.observacoes = observacoes;
        this.valorTotal = BigDecimal.ZERO;
    }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        item.setVenda(this);
        recalcularValorTotal();
    }

    public void recalcularValorTotal() {
        this.valorTotal = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cancelar() {
        if ("CANCELADA".equalsIgnoreCase(this.status)) {
            throw new RegraDeNegocioException("Esta venda já se encontra cancelada!");
        }
        this.status = "CANCELADA";
    }
}
