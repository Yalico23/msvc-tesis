package com.tesis.proyect.app.infrastructure.controllers;


import com.tesis.proyect.app.infrastructure.config.security.JwtTokenProvider;
import com.tesis.proyect.app.infrastructure.config.security.filter.JwtAuthenticationManager;
import com.tesis.proyect.app.infrastructure.dto.request.LoginUserRequest;
import com.tesis.proyect.app.infrastructure.dto.response.LoginUserResponse;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final JwtAuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginUserResponse>> login(@Valid @RequestBody LoginUserRequest request) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        return authenticationManager.authenticate(authToken)
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(authentication -> {
                    String email = authentication.getName();
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    String role = authorities.iterator().next().getAuthority();

                    // Generar JWT
                    String token = generateToken(email, role);

                    LoginUserResponse response = LoginUserResponse.builder()
                            .token(token)
                            .email(email)
                            .role(role)
                            .build();

                    return Mono.just(ResponseEntity.ok(response));
                })
                .onErrorResume(BadCredentialsException.class, ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(LoginUserResponse.builder()
                                        .error("Credenciales inválidas")
                                        .build()))
                );
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(Duration.ofHours(5))))
                .signWith(JwtTokenProvider.SECRET_KEY)
                .compact();
    }
}
