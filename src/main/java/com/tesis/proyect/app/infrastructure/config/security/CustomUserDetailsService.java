package com.tesis.proyect.app.infrastructure.config.security;

import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return this.userRepositoryPort.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)))
                .map(user -> {
                    // Mapear el rol a SimpleGrantedAuthority
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(user.getRole().getName());

                    return User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .authorities(authority)
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(!user.getActive())
                            .build();
                });
    }
}
