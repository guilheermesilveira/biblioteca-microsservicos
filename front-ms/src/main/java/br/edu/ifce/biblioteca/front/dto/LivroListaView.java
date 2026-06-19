package br.edu.ifce.biblioteca.front.dto;

public record LivroListaView(
        Long id,
        String titulo,
        String genero,
        Integer anoPublicacao,
        Boolean disponivel,
        Long autorId,
        String autorNome
) {

    public static LivroListaView fromLivro(LivroDto livro, String autorNome) {
        return new LivroListaView(
                livro.id(),
                livro.titulo(),
                livro.genero(),
                livro.anoPublicacao(),
                livro.disponivel(),
                livro.autorId(),
                autorNome
        );
    }
}
