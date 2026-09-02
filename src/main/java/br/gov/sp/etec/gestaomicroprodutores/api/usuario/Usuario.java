package br.gov.sp.etec.gestaomicroprodutores.api.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Usuario")
@Table(name = "tb_usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "idUsuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 100)
    private String sobrenome;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;
    @Column(length = 20)
    private String telefone;
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
    @Column(nullable = false)
    private boolean ativo;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }

    public Usuario(String nome, String sobrenome, String email, String cpf, String telefone) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public void atualizarInformacoes(String nome, String sobrenome, String telefone) {
        if (nome != null && !nome.isBlank()) this.nome = nome;
        if (sobrenome != null && !sobrenome.isBlank()) this.sobrenome = sobrenome;
        if (telefone != null && !telefone.isBlank()) this.telefone = telefone;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

}
