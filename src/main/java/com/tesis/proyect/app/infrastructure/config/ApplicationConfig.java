package com.tesis.proyect.app.infrastructure.config;

import com.tesis.proyect.app.application.services.RolService;
import com.tesis.proyect.app.application.services.UserService;
import com.tesis.proyect.app.application.usecases.rol.CreateRolUseCaseImpl;
import com.tesis.proyect.app.application.usecases.user.CreateUserUseCaseImpl;
import com.tesis.proyect.app.domain.ports.output.RolRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import com.tesis.proyect.app.infrastructure.repositories.RolEntityAdapter;
import com.tesis.proyect.app.infrastructure.repositories.UserEntityAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
    @Bean
    public UserRepositoryPort userRepositoryPort(UserEntityAdapter userEntityAdapter) {
        return userEntityAdapter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(UserRepositoryPort userRepositoryPort, RolRepositoryPort rolRepositoryPort, PasswordEncoder passwordEncoder) {
        return new UserService(
                new CreateUserUseCaseImpl(userRepositoryPort, rolRepositoryPort, passwordEncoder)
        );
    }

    @Bean
    public RolRepositoryPort rolRepositoryPort(RolEntityAdapter rolEntityAdapter) {
        return rolEntityAdapter;
    }

    @Bean
    public RolService rolService(RolRepositoryPort rolRepositoryPort) {
        return new RolService(
                new CreateRolUseCaseImpl(rolRepositoryPort)
        );
    }

}
