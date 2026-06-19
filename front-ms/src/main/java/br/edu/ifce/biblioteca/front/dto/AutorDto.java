package br.edu.ifce.biblioteca.front.dto;

public record AutorDto(
        Long id,
        String nome,
        String nacionalidade,
        Integer anoNascimento
) {
}
