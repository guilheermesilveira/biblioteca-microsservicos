package br.edu.ifce.biblioteca.autor.dto;

import br.edu.ifce.biblioteca.autor.entity.Autor;

public record AutorResponse(
        Long id,
        String nome,
        String nacionalidade,
        Integer anoNascimento
) {

    public static AutorResponse fromEntity(Autor autor) {
        return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                autor.getNacionalidade(),
                autor.getAnoNascimento()
        );
    }
}
