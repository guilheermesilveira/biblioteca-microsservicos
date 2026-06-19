package br.edu.ifce.biblioteca.livro.exception;

public class LivroNotFoundException extends RuntimeException {

    public LivroNotFoundException(Long id) {
        super("Livro não encontrado: " + id);
    }
}
