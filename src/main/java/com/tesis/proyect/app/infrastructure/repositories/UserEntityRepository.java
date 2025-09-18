package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.infrastructure.documents.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserEntityRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> save (UserEntity userEntity);
    Mono<UserEntity> findByToken(String token);
    Mono<UserEntity> findByEmail(String email);
    Flux<UserEntity> findAllByRoleName(String roleName);
}
