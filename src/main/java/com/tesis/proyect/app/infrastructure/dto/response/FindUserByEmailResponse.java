package com.tesis.proyect.app.infrastructure.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindUserByEmailResponse {
    private String id;
    private String name;
    private String lastName;
    private String email;
}
