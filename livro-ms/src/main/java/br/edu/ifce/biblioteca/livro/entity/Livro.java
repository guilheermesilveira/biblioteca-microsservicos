package br.edu.ifce.biblioteca.livro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 60)
    private String genero;

    @Column(nullable = false)
    private Integer anoPublicacao;

    @Column(nullable = false)
    private boolean disponivel = true;

    @Column(nullable = false)
    private Long autorId;

    protected Livro() {
    }

    public Livro(String titulo, String genero, Integer anoPublicacao, boolean disponivel, Long autorId) {
        this.titulo = titulo;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = disponivel;
        this.autorId = autorId;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void atualizar(String titulo, String genero, Integer anoPublicacao, boolean disponivel, Long autorId) {
        this.titulo = titulo;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = disponivel;
        this.autorId = autorId;
    }
}
