package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.application.services.RolService;
import com.tesis.proyect.app.domain.models.Rol;
import com.tesis.proyect.app.infrastructure.dto.request.CreateRolRequest;
import com.tesis.proyect.app.infrastructure.mappers.RolMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/role")
public class RolController {

    private final RolService rolService;
    private final RolMapper rolMapper;

    public RolController(RolService rolService, RolMapper rolMapper) {
        this.rolService = rolService;
        this.rolMapper = rolMapper;
    }

    //@PreAuthorize("hasAnyRole('ADMIN','RECLUTADOR')")
    @PostMapping("/crear")
    public ResponseEntity<Mono<Rol>> createTol(@Valid @RequestBody CreateRolRequest rol) {
        return ResponseEntity.ok(rolService.createRol(rolMapper.toModel(rol)));
    }
}
