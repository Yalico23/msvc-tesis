package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.UserService;
import com.tesis.proyect.app.infrastructure.config.security.AuthService;
import com.tesis.proyect.app.infrastructure.dto.request.LoginUserRequest;
import com.tesis.proyect.app.infrastructure.dto.response.LoginUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Mono<ResponseEntity<LoginUserResponse>> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {

        return this.authService.authenticate(loginUserRequest.getEmail(), loginUserRequest.getPassword())
                .flatMap(jwt -> userService.findByEmail(loginUserRequest.getEmail())
                        .map(user -> {
                            String roleName = user.getRole() != null ? user.getRole().getName() : null;

                            LoginUserResponse response = new LoginUserResponse(
                                    jwt,
                                    user.getEmail(),
                                    roleName
                            );
                            return ResponseEntity.ok(response);
                        })
                );
    }
}