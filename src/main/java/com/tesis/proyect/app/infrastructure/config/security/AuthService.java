package com.tesis.proyect.app.infrastructure.config.security;

import com.tesis.proyect.app.domain.exceptions.InvalidCredentialsException;
import com.tesis.proyect.app.domain.exceptions.NoActiveUserException;
import com.tesis.proyect.app.infrastructure.config.security.helpers.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;


    public Mono<String> authenticate(String email, String password) {
        return this.customUserDetailsService.findByUsername(email)
                .flatMap(userDetails -> {
                    // Verificar si está activo
                    if (!userDetails.isEnabled()) {
                        return Mono.error(new NoActiveUserException("Usuario no activo"));
                    }
                    // Verificar contraseña
                    if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
                        return Mono.error(new InvalidCredentialsException("Invalid email or password"));
                    }
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();

                    return Mono.just(this.jwtHelper.generateJwt(email, roles));
                });
    }
}
