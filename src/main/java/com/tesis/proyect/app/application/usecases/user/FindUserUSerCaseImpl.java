package com.tesis.proyect.app.application.usecases.user;

import com.tesis.proyect.app.domain.exceptions.UserNotFoundException;
import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.user.FindUserUSerCase;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import com.tesis.proyect.app.infrastructure.dto.response.ListPartResponse;
import com.tesis.proyect.app.utils.EstadoEntrevista;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindUserUSerCaseImpl implements FindUserUSerCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserInterviewRepositoryPort userInterviewRepositoryPort;

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepositoryPort.findByEmail(email).
                switchIfEmpty(Mono.error(new UserNotFoundException("User with email " + email + " not found")));
    }

    @Override
    public Flux<User> findByRoleName(String roleName) {
        return userRepositoryPort.findByRoleName(roleName);
    }

    @Override
    public Flux<ListPartResponse> findByRolNameAnsStatus() {
        return userRepositoryPort.findByRoleName("ROLE_PRACTICANTE")
                .flatMap(user ->
                        userInterviewRepositoryPort.findByUserId(user.getId())
                                .map(userInterview -> buildListPartResponse(user, userInterview))
                                .switchIfEmpty(Mono.defer(() -> {
                                    System.out.println("[WARN] Usuario sin entrevista asignada: " + user.getEmail());
                                    return Mono.just(buildListPartResponse(user, null));
                                }))
                )
                .doOnNext(res -> System.out.println("[INFO] Usuario cargado: " + res.getEmail()))
                .doOnError(e -> System.err.println("[ERROR] Error obteniendo usuarios: " + e.getMessage()));
    }

    private ListPartResponse buildListPartResponse(User user, UserInterview userInterview) {
        return ListPartResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .interviewStatus(
                        userInterview != null ? userInterview.getState() : EstadoEntrevista.NO_ASIGNADA.name()
                )
                .build();
    }
}
