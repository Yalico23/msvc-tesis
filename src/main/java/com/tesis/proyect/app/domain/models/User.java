package com.tesis.proyect.app.domain.models;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String token;
    private LocalDate creationDate;
    private Boolean active = false;
    private Rol role;
    private List<UserInterview> userInterviews;
    private List<InterviewRef> interviews;
    // Only for Requests no transient to persist
    private String rolName;
}