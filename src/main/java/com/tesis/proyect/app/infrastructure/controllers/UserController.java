package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.UserService;
import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.infrastructure.dto.request.CreateUserRequest;
import com.tesis.proyect.app.infrastructure.mappers.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<User>> createUser(@Valid  @RequestBody CreateUserRequest request) {
        return userService.createUser(userMapper.toModel(request))
                .map(user -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(user)
                );
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping("/postulante")
    public Mono<String> adminAccess() {
        return Mono.just("Postulante access granted");
    }

    @PreAuthorize("hasRole('RECLUTADOR')")
    @GetMapping("/reclutador")
    public Mono<String> userAccess() {
        return Mono.just("Reclutador access granted");
    }
}
