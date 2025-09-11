package com.tesis.proyect.app.infrastructure.config.security.helpers;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtHelper {

    public String generateJwt(String subject, List<String> roles) {
        final Date now = new Date();
        final Date exp = Date.from(Instant.now().plus(Duration.ofMinutes(3)));

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(exp)
                .claims(Map.of("roles", roles))
                .signWith(this.getSecretKey())
                .compact();
    }

    @SuppressWarnings("unchecked") // Para evitar advertencias de conversión
    public List<String> getRolesFromJwt(String jwt) {
        Claims claims = this.getClaimsFromJwt(jwt);
        return (List<String>) claims.get("roles");
    }

    public boolean validateJwt(String token){
        try {
            final Claims claims = this.getClaimsFromJwt(token);
            final Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (ExpiredJwtException e) {
            log.debug("Token JWT expirado: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.debug("Token JWT malformado: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.debug("Error en token JWT: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error inesperado validando JWT: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromJwt(String jwt) {
        return this.getClaimsFromJwt(jwt).getSubject();
    }

    private SecretKey getSecretKey() {
        final String secretKey  = "mysecretkeymysecretkeymysecretkey";
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaimsFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(this.getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
