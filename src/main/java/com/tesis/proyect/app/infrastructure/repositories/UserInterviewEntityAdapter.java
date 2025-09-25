package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.infrastructure.mappers.UserInterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class UserInterviewEntityAdapter implements UserInterviewRepositoryPort {

    private final UserInterviewMapper mapper;
    private final UserInterviewEntityRespository userInterviewEntityRespository;

    @Override
    public Mono<UserInterview> save(UserInterview userInterview) {
        return userInterviewEntityRespository.save(mapper.toEntity(userInterview)).
                map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByUserId(String userId) {
        return userInterviewEntityRespository.existsByUserId(userId);
    }

    @Override
    public Mono<UserInterview> findByUserId(String userId) {
        return userInterviewEntityRespository.findByUserId(userId)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<UserInterview> findAll() {
        return userInterviewEntityRespository.findAll().
                map(mapper::toDomain);
    }

    @Override
    public Mono<UserInterview> findByUserIdAndInterviewId(String userId, String interviewId) {
        return userInterviewEntityRespository.findByUserIdAndInterviewId(userId, interviewId).
                map(mapper::toDomain);
    }
}
