package com.tesis.proyect.app.domain.models;

import lombok.*;

import java.time.LocalDate;

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
    private Boolean active;
    private Rol role;
    private String rolName;
    private String interviewAsignedId;
}