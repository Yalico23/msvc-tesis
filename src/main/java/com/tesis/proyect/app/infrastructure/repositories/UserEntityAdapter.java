package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import com.tesis.proyect.app.infrastructure.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class UserEntityAdapter implements UserRepositoryPort {

    private final UserEntityRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<User> save(User user) {
        return repository.save(mapper.toEntity(user))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existByToken(String token) {
        return repository.findByToken(token).hasElement();
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).
                map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existByEmail(String email) {
        return repository.findByEmail(email).hasElement();
    }

    @Override
    public Flux<User> findByRoleName(String roleName) {
        return repository.findAllByRoleName(roleName)
                .map(mapper::toModel);
    }
}
