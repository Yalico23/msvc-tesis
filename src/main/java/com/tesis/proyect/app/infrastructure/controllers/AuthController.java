package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.UserService;
import com.tesis.proyect.app.infrastructure.config.security.AuthService;
import com.tesis.proyect.app.infrastructure.dto.request.LoginUserRequest;
import com.tesis.proyect.app.infrastructure.dto.response.LoginUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginUserResponse>> login(@RequestBody LoginUserRequest loginUserRequest) {
        log.info("Intentando login con email={}", loginUserRequest.getEmail());

        return this.authService.authenticate(loginUserRequest.getEmail(), loginUserRequest.getPassword())
                .doOnNext(jwt -> log.info("JWT generado correctamente: {}", jwt))
                .flatMap(jwt -> userService.findByEmail(loginUserRequest.getEmail())
                        .doOnNext(user -> log.info("Usuario encontrado en BD: {}", user.getEmail()))
                        .map(user -> {
                            String roleName = user.getRole() != null ? user.getRole().getName() : null;

                            LoginUserResponse response = new LoginUserResponse(
                                    jwt,
                                    user.getEmail(),
                                    roleName
                            );

                            return ResponseEntity.ok(response);
                        })
                )
                .onErrorResume(err -> {
                    log.error("Error en login: {}", err.getMessage(), err);
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

}
