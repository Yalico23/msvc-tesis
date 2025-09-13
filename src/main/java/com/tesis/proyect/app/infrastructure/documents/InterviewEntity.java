package com.tesis.proyect.app.infrastructure.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interviews")
public class InterviewEntity {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDate createdAt;
    private Boolean active;
    private String userId;
    private List<QuestionEntity> questions;
}
