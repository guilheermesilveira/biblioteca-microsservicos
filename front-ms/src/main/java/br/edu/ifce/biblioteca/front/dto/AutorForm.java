package br.edu.ifce.biblioteca.front.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AutorForm {

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe a nacionalidade")
    private String nacionalidade;

    @NotNull(message = "Informe o ano de nascimento")
    @Positive(message = "O ano de nascimento deve ser positivo")
    private Integer anoNascimento;

    public AutorForm() {
    }

    public AutorForm(String nome, String nacionalidade, Integer anoNascimento) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.anoNascimento = anoNascimento;
    }

    public static AutorForm fromDto(AutorDto autor) {
        return new AutorForm(autor.nome(), autor.nacionalidade(), autor.anoNascimento());
    }

    public AutorDto toDto() {
        return new AutorDto(null, nome, nacionalidade, anoNascimento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }
}
