package br.edu.ifce.biblioteca.autor.exception;

public class AutorNotFoundException extends RuntimeException {

    public AutorNotFoundException(Long id) {
        super("Autor não encontrado: " + id);
    }
}
