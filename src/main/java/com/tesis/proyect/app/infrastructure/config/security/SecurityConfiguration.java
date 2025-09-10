package com.tesis.proyect.app.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.proyect.app.domain.models.ErrorResponse;
import com.tesis.proyect.app.infrastructure.config.security.filter.JwtAuthenticationManager;
import com.tesis.proyect.app.infrastructure.config.security.filter.JwtServerAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtServerAuthenticationConverter jwtConverter;

    SecurityConfiguration(JwtAuthenticationManager jwtAuthenticationManager, JwtServerAuthenticationConverter jwtConverter) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtConverter = jwtConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        jwtFilter.setServerAuthenticationConverter(jwtConverter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges.pathMatchers(
                                        "/api/*/**",
                                        "/swagger-ui.html",      // redirect
                                        "/swagger-ui/**",        // UI actual
                                        "/v3/api-docs/**",       // JSON de la API
                                        "/swagger-resources/**", // recursos swagger
                                        "/webjars/**",           // estáticos
                                        "/api/user/v1/create"    // endpoint público
                                ).permitAll()
                                .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((exchange, ex) -> {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            response.getHeaders().add("Content-Type", "application/json");

                            ErrorResponse errorUnauthorized = ErrorResponse.builder()
                                    .code("401")
                                    .message("No autorizado: token inválido o ausente")
                                    .details(List.of(ex.getMessage())) // puedes añadir más detalles
                                    .timestamp(LocalDateTime.now())
                                    .build();

                            try {
                                String body = new ObjectMapper().writeValueAsString(errorUnauthorized);
                                DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                                return response.writeWith(Mono.just(buffer));
                            } catch (Exception e) {
                                DataBuffer buffer = response.bufferFactory().wrap("{\"error\":\"serialization failed\"}".getBytes());
                                return response.writeWith(Mono.just(buffer));
                            }
                        })
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
