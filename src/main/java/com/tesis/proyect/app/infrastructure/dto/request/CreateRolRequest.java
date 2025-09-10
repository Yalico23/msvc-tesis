package com.tesis.proyect.app.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateRolRequest(
        @NotBlank(message = "El nombre no puede ser nulo")
        String name
) {
}
