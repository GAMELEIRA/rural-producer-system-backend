package br.gov.sp.etec.gestaomicroprodutores.api.talhao;

import br.gov.sp.etec.gestaomicroprodutores.api.talhao.dto.DadosCadastroTalhao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "Talhao")
@Table(name = "tb_talhao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idTalhao")
public class Talhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_talhao")
    private Long idTalhao;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal area;
    @Column(name = "unidade_area", nullable = false, length = 20)
    private String unidadeArea;
    @Column(nullable = false, length = 200)
    private String localizacao;
    @Column(length = 500)
    private String observacoes;

    public Talhao(DadosCadastroTalhao dados) {
        atualizarInformacoes(dados);
    }

    public void atualizarInformacoes(DadosCadastroTalhao dados) {
        this.nome = dados.nome();
        this.area = dados.area();
        this.unidadeArea = dados.unidadeArea();
        this.localizacao = dados.localizacao();
        this.observacoes = dados.observacoes();
    }
}
