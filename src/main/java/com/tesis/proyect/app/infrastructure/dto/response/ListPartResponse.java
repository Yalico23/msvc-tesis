package com.tesis.proyect.app.infrastructure.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListPartResponse {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String interviewStatus;
}
