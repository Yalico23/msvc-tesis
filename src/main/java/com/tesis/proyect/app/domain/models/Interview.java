package com.tesis.proyect.app.domain.models;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    private String id; // MongoDB usa String para _id por defecto
    private String title;
    private String description;
    private LocalDate createdAt;
    private Boolean active;
    private String userId; // ID del usuario que creó la entrevista
    private List<Question> questions;
}
