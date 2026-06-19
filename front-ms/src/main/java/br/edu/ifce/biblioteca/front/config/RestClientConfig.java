package br.edu.ifce.biblioteca.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient autorRestClient(@Value("${biblioteca.api.autores-base-url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    public RestClient livroRestClient(@Value("${biblioteca.api.livros-base-url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
