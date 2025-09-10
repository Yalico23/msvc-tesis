package com.tesis.proyect.app.infrastructure.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "questions")
public class QuestionEntity {
    @Id
    private String id;
    private String text;
    private Integer points;
    private Integer time;
    private InterviewEntity interview;
}
