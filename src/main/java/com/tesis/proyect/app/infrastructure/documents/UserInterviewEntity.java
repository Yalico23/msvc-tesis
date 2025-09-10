package com.tesis.proyect.app.infrastructure.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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
    private UserEntity user;
    private InterviewEntity interview;
}
