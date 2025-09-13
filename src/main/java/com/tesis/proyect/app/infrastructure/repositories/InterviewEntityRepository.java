package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.infrastructure.documents.InterviewEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface InterviewEntityRepository extends ReactiveMongoRepository<InterviewEntity,String> {
    Mono<InterviewEntity> save (InterviewEntity token);
    Flux<InterviewEntity> findAll();
}
