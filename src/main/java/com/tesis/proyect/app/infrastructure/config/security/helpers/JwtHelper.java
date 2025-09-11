package com.tesis.proyect.app.infrastructure.config.security.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtHelper {

    public String generateJwt(String subject, List<String> roles) {
        final Date now = new Date();
        final Date exp = Date.from(Instant.now().plus(Duration.ofHours(5)));

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

    public boolean validateJwt(String token) {
        try {
            final Claims claims = this.getClaimsFromJwt(token);
            final Date expiration = claims.getExpiration();
            return  expiration.after(new Date());
        }catch (Exception e) {
            return  false;
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
