package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String username, boolean admin) {
        return Jwts.builder()
                .setSubject(username)
                .claim("admin", admin)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String generateUnsignedToken(String username, boolean admin) {
        return Jwts.builder()
                .setSubject(username)
                .claim("admin", admin)

                .setHeaderParam("alg", "none")

                .compact();
    }

    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
}
