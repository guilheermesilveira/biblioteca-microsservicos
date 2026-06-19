package br.edu.ifce.biblioteca.livro.dto;

import br.edu.ifce.biblioteca.livro.entity.Livro;

public record LivroResponse(
        Long id,
        String titulo,
        String genero,
        Integer anoPublicacao,
        boolean disponivel,
        Long autorId
) {

    public static LivroResponse fromEntity(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getGenero(),
                livro.getAnoPublicacao(),
                livro.isDisponivel(),
                livro.getAutorId()
        );
    }
}
