package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.config.VulnerabilityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.owasp.encoder.Encode;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VulnerabilityConfig vulnerabilityConfig;

    @GetMapping
    public List<Map<String, Object>> getAllProducts() {
        return jdbcTemplate.queryForList("SELECT name FROM products");
    }

    @GetMapping("/{name}")
    public Object getProductDetails(
            @PathVariable String name,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {


        Boolean isRestricted;
        if (vulnerabilityConfig.isEnabled("SQL_INJECTION_FRUITS")) {

            String sql = "SELECT restricted FROM products WHERE name = '" + name + "'";
            try {
                isRestricted = jdbcTemplate.queryForObject(sql, Boolean.class);
            } catch (Exception e) {

                isRestricted = false;
            }
        } else {
            // Query parametrizzata (sicura)
            try {
                isRestricted = jdbcTemplate.queryForObject(
                        "SELECT restricted FROM products WHERE name = ?",
                        Boolean.class,
                        name
                );
            } catch (Exception e) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato");
            }
        }


        if (!isRestricted) {
            if (vulnerabilityConfig.isEnabled("SQL_INJECTION_FRUITS")) {
                // Query vulnerabile
                String sql = "SELECT * FROM products WHERE name = '" + name + "'";
                try {
                    return jdbcTemplate.queryForMap(sql);
                } catch (Exception e) {
                    return jdbcTemplate.queryForList(sql);
                }
            } else {
                // Query parametrizzata (sicura)
                return jdbcTemplate.queryForMap(
                        "SELECT * FROM products WHERE name = ?",
                        name
                );
            }
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token mancante");
        }

        String token = authHeader.substring(7);
        String username;
        Boolean isAdmin;

        try {
            Claims claims;
            if (vulnerabilityConfig.isEnabled("JWT_UNSIGNED")) {
                claims = Jwts.parserBuilder()
                        .build()
                        .parseClaimsJwt(token)
                        .getBody();
            } else {
                claims = Jwts.parserBuilder()
                        .setSigningKey(JwtUtil.getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            }

            username = claims.getSubject();
            isAdmin = claims.get("admin", Boolean.class);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido");
        }

        if (isAdmin == null || !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accesso negato");
        }

        if (vulnerabilityConfig.isEnabled("SQL_INJECTION_FRUITS")) {
            String sql = "SELECT * FROM products WHERE name = '" + name + "'";
            try {
                return jdbcTemplate.queryForMap(sql);
            } catch (Exception e) {
                return jdbcTemplate.queryForList(sql);
            }
        } else {
            return jdbcTemplate.queryForMap(
                    "SELECT * FROM products WHERE name = ?",
                    name
            );
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateProductDescription(
            @PathVariable String name,
            @RequestBody Map<String, String> request) {

        if (!request.containsKey("description") || request.get("description").isBlank()) {
            return ResponseEntity.badRequest().body("La descrizione è obbligatoria");
        }

        String newDescription = request.get("description");

        if (!vulnerabilityConfig.isEnabled("XSS_STORED")) {
            newDescription = Encode.forHtml(newDescription);
        }

        int updatedRows = jdbcTemplate.update(
                "UPDATE products SET description = ? WHERE name = ?",
                newDescription, name
        );

        if (updatedRows > 0) {
            return ResponseEntity.ok().body("Descrizione aggiornata con successo");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
