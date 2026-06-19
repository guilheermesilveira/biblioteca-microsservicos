package br.edu.ifce.biblioteca.front.client;

public class ApiUnavailableException extends RuntimeException {

    public ApiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
