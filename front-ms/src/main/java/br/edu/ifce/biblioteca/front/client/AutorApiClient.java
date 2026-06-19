package br.edu.ifce.biblioteca.front.client;

import br.edu.ifce.biblioteca.front.dto.AutorDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class AutorApiClient {

    private final RestClient restClient;
    private final ApiErrorParser errorParser;

    public AutorApiClient(@Qualifier("autorRestClient") RestClient restClient, ApiErrorParser errorParser) {
        this.restClient = restClient;
        this.errorParser = errorParser;
    }

    public List<AutorDto> listar() {
        try {
            return restClient.get()
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel carregar os autores.", exception);
        }
    }

    public AutorDto buscar(Long id) {
        try {
            return restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .body(AutorDto.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new ApiNotFoundException(errorParser.extractErrorMessage(exception, "Autor nao encontrado."));
            }
            throw new ApiUnavailableException("Nao foi possivel buscar o autor.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel buscar o autor.", exception);
        }
    }

    public AutorDto cadastrar(AutorDto autor) {
        try {
            return restClient.post()
                    .body(autor)
                    .retrieve()
                    .body(AutorDto.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))) {
                throw new ApiValidationException(errorParser.extractValidationErrors(exception));
            }
            throw new ApiUnavailableException("Nao foi possivel cadastrar o autor.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel cadastrar o autor.", exception);
        }
    }

    public AutorDto atualizar(Long id, AutorDto autor) {
        try {
            return restClient.put()
                    .uri("/{id}", id)
                    .body(autor)
                    .retrieve()
                    .body(AutorDto.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))) {
                throw new ApiValidationException(errorParser.extractValidationErrors(exception));
            }
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new ApiNotFoundException(errorParser.extractErrorMessage(exception, "Autor nao encontrado."));
            }
            throw new ApiUnavailableException("Nao foi possivel atualizar o autor.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel atualizar o autor.", exception);
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
                throw new ApiNotFoundException(errorParser.extractErrorMessage(exception, "Autor nao encontrado."));
            }
            throw new ApiUnavailableException("Nao foi possivel excluir o autor.", exception);
        } catch (RestClientException exception) {
            throw new ApiUnavailableException("Nao foi possivel excluir o autor.", exception);
        }
    }
}
