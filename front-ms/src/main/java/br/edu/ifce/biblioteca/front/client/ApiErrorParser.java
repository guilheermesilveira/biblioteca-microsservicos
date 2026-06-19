package br.edu.ifce.biblioteca.front.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ApiErrorParser {

    private final ObjectMapper objectMapper;

    public ApiErrorParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractErrorMessage(RestClientResponseException exception, String fallback) {
        try {
            JsonNode root = objectMapper.readTree(exception.getResponseBodyAsByteArray());
            if (root.hasNonNull("erro")) {
                return root.get("erro").asString();
            }
        } catch (JacksonException ignored) {
            return fallback;
        }
        return fallback;
    }

    public Map<String, String> extractValidationErrors(RestClientResponseException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        try {
            JsonNode root = objectMapper.readTree(exception.getResponseBodyAsByteArray());
            if (root.has("erros")) {
                JsonNode errorNode = root.get("erros");
                for (Map.Entry<String, JsonNode> entry : errorNode.properties()) {
                    errors.put(entry.getKey(), entry.getValue().asString());
                }
                return errors;
            }
            if (root.hasNonNull("erro")) {
                errors.put("geral", root.get("erro").asString());
            }
        } catch (JacksonException ignored) {
            errors.put("geral", "Não foi possível processar o erro retornado pela API.");
        }
        if (errors.isEmpty()) {
            errors.put("geral", "A API rejeitou os dados enviados.");
        }
        return errors;
    }
}
