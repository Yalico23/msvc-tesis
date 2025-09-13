package com.tesis.proyect.app.application.usecases.user;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.input.user.CreateUserUseCase;
import com.tesis.proyect.app.domain.ports.output.RolRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RolRepositoryPort rolRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> createUser(User user) {
        return userRepositoryPort.existByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Email already registered"));
                    }
                    return rolRepositoryPort.findByRolName(user.getRolName())
                            .switchIfEmpty(Mono.error(new RuntimeException("Rol not found")))
                            .flatMap(role -> generateToken()
                                    .map(token -> {
                                        user.setRole(role);
                                        user.setCreationDate(LocalDate.now());
                                        user.setToken(token);
                                        user.setActive(false);
                                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                                        return user;
                                    })
                            )
                            .flatMap(userRepositoryPort::save);
                });
    }


    private Mono<String> generateToken() {
        return Mono.defer(() -> {
            String token = UUID.randomUUID().toString().replace("-", "");
            return userRepositoryPort.existByToken(token) // Mono<Boolean>
                    .flatMap(exists -> {
                        if (exists) {
                            // si ya existe, generar otro
                            return generateToken();
                        } else {
                            return Mono.just(token);
                        }
                    });
        });
    }
}
