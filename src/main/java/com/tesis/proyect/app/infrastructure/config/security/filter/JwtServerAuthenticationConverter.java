package com.tesis.proyect.app.infrastructure.config.security.filter;

import com.tesis.proyect.app.infrastructure.config.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter  implements ServerAuthenticationConverter  {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(JwtTokenProvider.HEADER_AUTHORIZATION))
                .filter(header -> header.startsWith(JwtTokenProvider.PREFIX_TOKEN))
                .map(header -> header.replace(JwtTokenProvider.PREFIX_TOKEN, ""))
                .map(this::validateToken)
                .onErrorResume(e -> Mono.empty());
    }

    private Authentication validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(JwtTokenProvider.SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            String role = claims.get("role", String.class); // ✅ directo, no array

            GrantedAuthority authority = new SimpleGrantedAuthority(role);

            return new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
        } catch (Exception e) {
            throw new RuntimeException("Token inválido", e);
        }
    }
}
