package com.tesis.proyect.app.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    private UserRef user;
    private List<Question> questions;
    private List<UserInterview> userInterviews;
}
