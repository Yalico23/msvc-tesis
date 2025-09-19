package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import com.tesis.proyect.app.infrastructure.mappers.InterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class InterviewEntityAdapter implements InterviewRepositoryPort {

    private final InterviewEntityRepository interviewEntityRepository;
    private final InterviewMapper mapper;

    @Override
    public Mono<Interview> save(Interview interview) {
        return interviewEntityRepository.save(mapper.toEntity(interview))
                .map(mapper::toModel);
    }

    @Override
    public Flux<Interview> findAll() {
        return interviewEntityRepository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<Interview> findById(String id) {
        return interviewEntityRepository.findById(id)
                .map(mapper::toModel);
    }
}
