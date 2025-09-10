package com.tesis.proyect.app.infrastructure.config.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtTokenProvider  {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build(); // se genera una key
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String PREFIX_TOKEN = "Bearer ";
}
