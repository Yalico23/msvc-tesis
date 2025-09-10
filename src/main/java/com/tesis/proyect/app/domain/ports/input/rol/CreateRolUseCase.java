package com.tesis.proyect.app.domain.ports.input.rol;

import com.tesis.proyect.app.domain.models.Rol;
import reactor.core.publisher.Mono;

public interface CreateRolUseCase {
    Mono<Rol> createRol(Rol rol);
}
