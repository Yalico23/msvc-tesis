package com.tesis.proyect.app.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInterview {
    private String id;
    private Integer score;
    private String state;
    private LocalDate date;
    private String userId; // quien realiza la entrevista
    private String interviewId; // entrevista realizada
    private String s3KeyPath; // path en s3
    private List<Answer> answers;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer{
        private String questionText;
        private String responseText;
    }
}
