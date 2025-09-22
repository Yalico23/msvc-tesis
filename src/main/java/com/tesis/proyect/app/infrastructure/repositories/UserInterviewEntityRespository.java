package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.infrastructure.documents.UserInterviewEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserInterviewEntityRespository extends ReactiveMongoRepository<UserInterviewEntity, String> {
    Mono<UserInterviewEntity> save (UserInterviewEntity userInterview);
    Flux<UserInterviewEntity> findAllByInterviewId(String interviewId);
    Mono<Boolean> existsByUserId(String userId);
    Flux<UserInterviewEntity> findAll(Sort sort);
}
