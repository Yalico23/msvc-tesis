package com.tesis.proyect.app.infrastructure.config.security;

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
                .filter(userDetails -> this.passwordEncoder.matches(password, userDetails.getPassword()))
                .map(userDetails -> {
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();

                    return this.jwtHelper.generateJwt(email,roles);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid Credentials")));
    }
}
