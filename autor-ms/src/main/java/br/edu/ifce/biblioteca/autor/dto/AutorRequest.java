package br.edu.ifce.biblioteca.autor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AutorRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "Nacionalidade é obrigatória")
        @Size(max = 80, message = "Nacionalidade deve ter no máximo 80 caracteres")
        String nacionalidade,

        @NotNull(message = "Ano de nascimento é obrigatório")
        @Positive(message = "Ano de nascimento deve ser positivo")
        Integer anoNascimento
) {
}
