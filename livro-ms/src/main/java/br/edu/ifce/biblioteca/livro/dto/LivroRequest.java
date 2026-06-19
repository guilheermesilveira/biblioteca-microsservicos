package br.edu.ifce.biblioteca.livro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LivroRequest(
        @NotBlank(message = "Titulo e obrigatorio")
        @Size(max = 150, message = "Titulo deve ter no maximo 150 caracteres")
        String titulo,

        @NotBlank(message = "Genero e obrigatorio")
        @Size(max = 60, message = "Genero deve ter no maximo 60 caracteres")
        String genero,

        @NotNull(message = "Ano de publicacao e obrigatorio")
        @Positive(message = "Ano de publicacao deve ser positivo")
        Integer anoPublicacao,

        Boolean disponivel,

        @NotNull(message = "Autor e obrigatorio")
        Long autorId
) {

    public boolean disponivelOuPadrao() {
        return disponivel == null || disponivel;
    }
}
