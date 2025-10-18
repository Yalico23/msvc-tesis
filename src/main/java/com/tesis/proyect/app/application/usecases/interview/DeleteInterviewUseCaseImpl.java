package com.tesis.proyect.app.application.usecases.interview;

import com.tesis.proyect.app.domain.ports.input.interview.DeleteInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteInterviewUseCaseImpl implements DeleteInterviewUseCase {

    private final InterviewRepositoryPort interviewRepositoryPort;
    private final UserInterviewRepositoryPort userInterviewRepositoryPort;

    @Override
    public Mono<Void> deleteInterview(String interviewId) {
        return userInterviewRepositoryPort.findByInterviewId(interviewId)
                .flatMap(userInterview -> {
                    if (userInterview.getInterviewId() == null || userInterview.getInterviewId().isEmpty()) {
                        // Si no hay entrevista asociada, procedemos a eliminarla
                        return interviewRepositoryPort.deleteById(interviewId)
                                .doOnSuccess(v -> System.out.println("[INFO] Entrevista eliminada: " + interviewId))
                                .doOnError(e -> System.err.println("[ERROR] No se pudo eliminar entrevista: " + e.getMessage()));
                    } else {
                        // Si existe una relación, devolvemos error controlado
                        return Mono.error(new IllegalStateException("Interview already exists for user"));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Si no se encuentra relación, igual eliminamos la entrevista principal
                    System.out.println("[WARN] No se encontró relación user-interview, eliminando entrevista directa");
                    return interviewRepositoryPort.deleteById(interviewId);
                }))
                .then(); // Convertimos el resultado en Mono<Void>
    }
}
