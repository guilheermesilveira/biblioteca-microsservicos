package br.edu.ifce.biblioteca.front.client;

import br.edu.ifce.biblioteca.front.dto.LivroDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class LivroApiClient {

    private final RestClient restClient;
    private final ApiErrorParser errorParser;

    public LivroApiClient(@Qualifier("livroRestClient") RestClient restClient, ApiErrorParser errorParser) {
        this.restClient = restClient;
        this.errorParser = errorParser;
    }

    public List<LivroDto> listar(Boolean disponivel) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> {
                        if (disponivel == null) {
                            return uriBuilder.build();
                        }
                        return uriBuilder.queryParam("disponivel", disponivel).build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel carregar os livros.", exception);
        }
    }

    public LivroDto buscar(Long id) {
        try {
            return restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .body(LivroDto.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new ApiNotFoundException(errorParser.extractErrorMessage(exception, "Livro nao encontrado."));
            }
            throw new ApiUnavailableException("Nao foi possivel buscar o livro.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel buscar o livro.", exception);
        }
    }

    public LivroDto cadastrar(LivroDto livro) {
        try {
            return restClient.post()
                    .body(livro)
                    .retrieve()
                    .body(LivroDto.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))) {
                throw new ApiValidationException(errorParser.extractValidationErrors(exception));
            }
            throw new ApiUnavailableException("Nao foi possivel cadastrar o livro.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel cadastrar o livro.", exception);
        }
    }

    public LivroDto atualizar(Long id, LivroDto livro) {
        try {
            return restClient.put()
                    .uri("/{id}", id)
                    .body(livro)
                    .retrieve()
                    .body(LivroDto.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))) {
                throw new ApiValidationException(errorParser.extractValidationErrors(exception));
            }
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new ApiNotFoundException(errorParser.extractErrorMessage(exception, "Livro nao encontrado."));
            }
            throw new ApiUnavailableException("Nao foi possivel atualizar o livro.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel atualizar o livro.", exception);
        }
    }

    public void excluir(Long id) {
        try {
            restClient.delete()
                    .uri("/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new ApiNotFoundException(errorParser.extractErrorMessage(exception, "Livro nao encontrado."));
            }
            throw new ApiUnavailableException("Nao foi possivel excluir o livro.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel excluir o livro.", exception);
        }
    }
}
