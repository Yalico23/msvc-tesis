package com.tesis.proyect.app.application.usecases.rol;

import com.tesis.proyect.app.domain.models.Rol;
import com.tesis.proyect.app.domain.ports.input.rol.CreateRolUseCase;
import com.tesis.proyect.app.domain.ports.output.RolRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateRolUseCaseImpl implements CreateRolUseCase {

    private final RolRepositoryPort repositoryPort;

    @Override
    public Mono<Rol> createRol(Rol rol) {
        return repositoryPort.save(rol);
    }
}
