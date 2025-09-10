package com.tesis.proyect.app.infrastructure.repositories;

import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    // Solo con 1 solo rol
    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepositoryPort.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)))
                .map(user -> {
                    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

                    return User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .disabled(!user.getActive())
                            .authorities(authority)
                            .build();
                });
    }
}
