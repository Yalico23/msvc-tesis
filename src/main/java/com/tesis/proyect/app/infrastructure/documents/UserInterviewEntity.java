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
@Document(collection = "userInterviews")
public class UserInterviewEntity {
    @Id
    private String id;
    private Integer score;
    private String state;
    private LocalDate date;
    private String userId; // quien realiza la entrevista
    private String interviewId; // entrevista realizada
    private String s3KeyPath; // path en s3
    private Integer duration; // duracion en minutos
    private List<Answer> answers;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer{
        private String questionText;
        private String responseText;
        private Integer points;
        private String description;
    }
}
