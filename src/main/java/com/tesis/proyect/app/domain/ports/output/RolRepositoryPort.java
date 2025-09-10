package com.tesis.proyect.app.domain.ports.output;

import com.tesis.proyect.app.domain.models.Rol;
import reactor.core.publisher.Mono;

public interface RolRepositoryPort {
    Mono<Rol> save (Rol rol);
    Mono<Rol> findByRolName(String rolName);
}
