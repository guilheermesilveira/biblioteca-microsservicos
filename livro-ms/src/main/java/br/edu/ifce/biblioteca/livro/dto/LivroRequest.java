package br.edu.ifce.biblioteca.livro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LivroRequest(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "Gênero é obrigatório")
        @Size(max = 60, message = "Gênero deve ter no máximo 60 caracteres")
        String genero,

        @NotNull(message = "Ano de publicação é obrigatório")
        @Positive(message = "Ano de publicação deve ser positivo")
        Integer anoPublicacao,

        Boolean disponivel,

        @NotNull(message = "Autor é obrigatório")
        Long autorId
) {

    public boolean disponivelOuPadrao() {
        return disponivel == null || disponivel;
    }
}
