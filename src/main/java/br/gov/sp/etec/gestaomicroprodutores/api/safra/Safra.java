package br.gov.sp.etec.gestaomicroprodutores.api.safra;

import br.gov.sp.etec.gestaomicroprodutores.api.plantacao.Plantacao;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosAtualizacaoSafra;
import br.gov.sp.etec.gestaomicroprodutores.api.safra.dto.DadosCadastroSafra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Safra")
@Table(name = "tb_safra")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idSafra")
public class Safra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_safra")
    private Long idSafra;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "safra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plantacao> plantacoes = new ArrayList<>();

    public Safra(DadosCadastroSafra dados) {
        this.nome = dados.nome().trim();
        this.dataInicio = dados.dataInicio();
        this.dataFim = dados.dataFim();
        this.status = (dados.status() != null && !dados.status().isBlank()) ? dados.status().trim().toUpperCase() : "EM_ANDAMENTO";
        this.observacoes = dados.observacoes();
    }

    public void atualizarInformacoes(DadosAtualizacaoSafra dados) {
        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome().trim();
        }
        if (dados.dataInicio() != null) {
            this.dataInicio = dados.dataInicio();
        }
        if (dados.dataFim() != null) {
            this.dataFim = dados.dataFim();
        }
        if (dados.status() != null && !dados.status().isBlank()) {
            this.status = dados.status().trim().toUpperCase();
        }
        if (dados.observacoes() != null) {
            this.observacoes = dados.observacoes();
        }
    }

    public void finalizarSafra() {
        this.status = "FINALIZADA";
    }
}
