package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.domain.models.Rol;
import com.tesis.proyect.app.domain.ports.output.RolRepositoryPort;
import com.tesis.proyect.app.infrastructure.mappers.RolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class RolEntityAdapter implements RolRepositoryPort {

    private final RolEntityRepository repository;
    private final RolMapper mapper;

    @Override
    public Mono<Rol> save(Rol rol) {
        return repository.save(mapper.toEntity(rol))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Rol> findByRolName(String rolName) {
        return repository.findByName(rolName)
                .map((mapper::toModel));
    }
}
