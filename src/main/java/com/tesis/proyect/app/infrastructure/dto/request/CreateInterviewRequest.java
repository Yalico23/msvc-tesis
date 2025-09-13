package com.tesis.proyect.app.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateInterviewRequest {
    @NotBlank(message = "El título no puede estar vacío")
    private String title;
    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;
    @NotNull(message = "Debe tener al menos una pregunta")
    private List<QuestionRequest> questions;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class QuestionRequest {
        @NotBlank(message = "El texto de la pregunta no puede estar vacío")
        private String text;
        @NotNull(message = "Los puntos no pueden ser nulos")
        private Integer points;
        @NotNull(message = "El tiempo no puede ser nulo")
        private Integer time;

    }
}
