package br.edu.ifce.biblioteca.front.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LivroForm {

    @NotBlank(message = "Informe o título")
    private String titulo;

    @NotBlank(message = "Informe o gênero")
    private String genero;

    @NotNull(message = "Informe o ano de publicação")
    @Positive(message = "O ano de publicação deve ser positivo")
    private Integer anoPublicacao;

    private Boolean disponivel = true;

    @NotNull(message = "Selecione um autor")
    private Long autorId;

    public LivroForm() {
    }

    public LivroForm(String titulo, String genero, Integer anoPublicacao, Boolean disponivel, Long autorId) {
        this.titulo = titulo;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = disponivel == null || disponivel;
        this.autorId = autorId;
    }

    public static LivroForm fromDto(LivroDto livro) {
        return new LivroForm(
                livro.titulo(),
                livro.genero(),
                livro.anoPublicacao(),
                livro.disponivel(),
                livro.autorId()
        );
    }

    public LivroDto toDto() {
        return new LivroDto(null, titulo, genero, anoPublicacao, disponivel == null || disponivel, autorId);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }
}
