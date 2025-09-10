package com.tesis.proyect.app.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInterview {
    private String id;
    private Integer score;
    private String state;
    private LocalDate date;
    private UserRef user;
    private InterviewRef interview;
}
