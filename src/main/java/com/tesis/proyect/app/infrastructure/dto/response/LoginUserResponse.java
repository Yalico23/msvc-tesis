package com.tesis.proyect.app.infrastructure.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponse {
    private String token;
    private String email;
    private String role;
    private long expiresIn;
    private String error;
}
