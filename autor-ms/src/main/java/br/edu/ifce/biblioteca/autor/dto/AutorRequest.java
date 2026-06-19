package br.edu.ifce.biblioteca.autor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AutorRequest(
        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
        String nome,

        @NotBlank(message = "Nacionalidade e obrigatoria")
        @Size(max = 80, message = "Nacionalidade deve ter no maximo 80 caracteres")
        String nacionalidade,

        @NotNull(message = "Ano de nascimento e obrigatorio")
        @Positive(message = "Ano de nascimento deve ser positivo")
        Integer anoNascimento
) {
}
