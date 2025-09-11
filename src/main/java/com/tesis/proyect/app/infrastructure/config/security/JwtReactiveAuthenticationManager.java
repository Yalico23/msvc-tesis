package com.tesis.proyect.app.infrastructure.config.security;

import com.tesis.proyect.app.domain.exceptions.JwtAuthenticationException;
import com.tesis.proyect.app.infrastructure.config.security.helpers.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtHelper jwtHelper;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String jwt = authentication.getCredentials().toString();

        if (!jwtHelper.validateJwt(jwt)) {
            return Mono.error(new JwtAuthenticationException("Invalid token o Expired token"));
        }
        return Mono.fromCallable(() -> {
            final String username = jwtHelper.getUsernameFromJwt(jwt);
            final List<String> roles = jwtHelper.getRolesFromJwt(jwt);
            final List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return UsernamePasswordAuthenticationToken.authenticated(username, null, authorities);
        });
    }
}
