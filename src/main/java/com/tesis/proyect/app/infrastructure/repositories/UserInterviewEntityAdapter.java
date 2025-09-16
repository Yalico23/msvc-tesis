package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.infrastructure.mappers.UserInterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
}
