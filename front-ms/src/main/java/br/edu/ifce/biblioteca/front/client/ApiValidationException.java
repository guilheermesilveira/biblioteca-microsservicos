package br.edu.ifce.biblioteca.front.client;

import java.util.Map;

public class ApiValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ApiValidationException(Map<String, String> errors) {
        super("Erro de validacao da API");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
