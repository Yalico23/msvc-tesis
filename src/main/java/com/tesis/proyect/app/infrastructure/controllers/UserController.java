package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.UserService;
import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.infrastructure.dto.request.CreateUserRequest;
import com.tesis.proyect.app.infrastructure.dto.response.FindUserByEmailResponse;
import com.tesis.proyect.app.infrastructure.dto.response.ListPracticantesResponse;
import com.tesis.proyect.app.infrastructure.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECLUTADOR')")
    @PostMapping("/create")
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(userMapper.toModel(request))
                .map(user -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(user)
                );
    }

    @PreAuthorize("hasAnyRole('RECLUTADOR','PARTICIPANTE')")
    @GetMapping("/findByEmail")
    public Mono<ResponseEntity<FindUserByEmailResponse>> getByEmail(@RequestParam("email") String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userMapper.toResponse(user)));
    }

    @PreAuthorize("hasRole('RECLUTADOR')")
    @GetMapping("/listPracticantes")
    public Mono<ResponseEntity<Flux<ListPracticantesResponse>>> listPracticantes() {
        Flux<ListPracticantesResponse> body = userService.findByRoleName("ROLE_PRACTICANTE")
                .map(userMapper::toListPracticantesResponse); // <-- aquí aplicas el mapper

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body));
    }
}
