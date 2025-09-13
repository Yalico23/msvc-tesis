package com.tesis.proyect.app.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerAuthenticationConverter converter;

    public SecurityConfiguration(ReactiveAuthenticationManager authenticationManager,
                                 ServerAuthenticationConverter converter) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",      // redirect
            "/swagger-ui/**",        // UI actual
            "/v3/api-docs/**",       // JSON de la API
            "/swagger-resources/**", // recursos swagger
            "/webjars/**",           // estáticos
            "/auth/**",               // endpoints de autenticación
    };

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.pathMatchers(
                                SWAGGER_WHITELIST).permitAll()
                                .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
