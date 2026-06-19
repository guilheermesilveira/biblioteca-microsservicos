package br.edu.ifce.biblioteca.front.dto;

public record LivroDto(
        Long id,
        String titulo,
        String genero,
        Integer anoPublicacao,
        Boolean disponivel,
        Long autorId
) {
}
