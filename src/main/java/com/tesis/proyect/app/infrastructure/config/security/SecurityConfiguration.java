package com.tesis.proyect.app.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerAuthenticationConverter converter;

    public SecurityConfiguration(ReactiveAuthenticationManager authenticationManager,
                                 ServerAuthenticationConverter converter) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.pathMatchers(
                                        "/swagger-ui.html",      // redirect
                                        "/swagger-ui/**",        // UI actual
                                        "/v3/api-docs/**",       // JSON de la API
                                        "/swagger-resources/**", // recursos swagger
                                        "/webjars/**",           // estáticos
                                        "/api/user/v1/create",    // endpoint público
                                        "/auth/**"                 // endpoint público
                                ).permitAll()
                                .pathMatchers("/api/user/v1/reclutador/**").hasRole("RECLUTADOR")
                                .pathMatchers("/api/user/v1/practicante/**").hasRole("PRACTICANTE")
                                .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
