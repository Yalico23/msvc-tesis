package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.Rol;
import com.tesis.proyect.app.domain.ports.input.rol.CreateRolUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RolService implements CreateRolUseCase {

    private final CreateRolUseCase createRolUseCase;

    public RolService(CreateRolUseCase createRolUseCase) {
        this.createRolUseCase = createRolUseCase;
    }

    @Override
    public Mono<Rol> createRol(Rol rol) {
        return createRolUseCase.createRol(rol);
    }
}
