package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.infrastructure.documents.RolEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RolEntityRepository extends ReactiveMongoRepository<RolEntity,String> {
    Mono<RolEntity> save (RolEntity rol);
    // Optional is redundant in reactive types
    Mono<RolEntity> findByName(String rolName);
}
