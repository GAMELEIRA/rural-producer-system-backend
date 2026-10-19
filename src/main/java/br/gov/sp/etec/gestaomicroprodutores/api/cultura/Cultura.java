package br.gov.sp.etec.gestaomicroprodutores.api.cultura;

import br.gov.sp.etec.gestaomicroprodutores.api.cultura.dto.DadosCadastroCultura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Cultura")
@Table(name = "tb_cultura")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idCultura")
public class Cultura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cultura")
    private Long idCultura;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 50)
    private String tipo;
    @Column(name = "ciclo_estimado", nullable = false)
    private Integer cicloEstimado;
    @Column(name = "unidade_ciclo", nullable = false, length = 20)
    private String unidadeCiclo;
    @Column(length = 500)
    private String observacoes;
    @Column(name = "imagem_codigo", length = 40)
    private String imagemCodigo;
    @Lob @Column(name = "imagem_personalizada", columnDefinition = "LONGTEXT")
    private String imagemPersonalizada;

    public Cultura(DadosCadastroCultura dados) {
        atualizarInformacoes(dados);
    }

    public void atualizarInformacoes(DadosCadastroCultura dados) {
        this.nome = dados.nome();
        this.tipo = dados.tipo();
        this.cicloEstimado = dados.cicloEstimado();
        this.unidadeCiclo = dados.unidadeCiclo();
        this.observacoes = dados.observacoes();
        this.imagemCodigo = dados.imagemCodigo();
        this.imagemPersonalizada = dados.imagemPersonalizada();
    }
}
