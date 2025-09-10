package com.tesis.proyect.app.infrastructure.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "roles")
public class RolEntity {
    @Id
    private String id; // MongoDB usa String para _id por defecto
    @Indexed(unique = true)
    private String name; // ej: ROLE_PRACTICANTE, ROLE_RECLUTADOR
}
