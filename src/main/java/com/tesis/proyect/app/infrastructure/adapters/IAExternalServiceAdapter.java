package com.tesis.proyect.app.infrastructure.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.proyect.app.domain.ports.output.IAExternalServicePort;
import com.tesis.proyect.app.infrastructure.dto.request.IARequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class IAExternalServiceAdapter implements IAExternalServicePort {

    private final WebClient webClient;

    @Value("${openai.ia.model}")
    private String iaModel;
    @Value("${openai.ia.api-key}")
    private String apiKey;
    @Value("${openai.ia.url}")
    private String apiUrl;

    @Override
    public Mono<String> getIaServiceResponse(String prompt) {
        IARequest iaRequest = IARequest.builder()
                .model(iaModel)
                .messages(List.of(new IARequest.IAMessage("user", prompt)))
                .build();

        return webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(iaRequest)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonResponse = objectMapper.readTree(response);

                        if (jsonResponse.has("choices") && !jsonResponse.get("choices").isEmpty()) {
                            return Mono.just(
                                    jsonResponse.get("choices").get(0).get("message").get("content").asText()
                            );
                        } else if (jsonResponse.has("error")) {
                            return Mono.error(new RuntimeException(
                                    "Error de la IA: " + jsonResponse.get("error").asText()
                            ));
                        } else {
                            return Mono.error(new RuntimeException("Respuesta inesperada de la IA"));
                        }
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error al parsear la respuesta: " + e.getMessage(), e));
                    }
                })
                .onErrorResume(e ->
                        Mono.error(new RuntimeException("Error al comunicarse con el servicio de IA: " + e.getMessage(), e))
                );
    }
}
