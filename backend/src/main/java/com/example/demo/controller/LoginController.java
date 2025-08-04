package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.config.VulnerabilityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VulnerabilityConfig vulnerabilityConfig;

    public static class AuthResponse {
        private final String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    @PostMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        try {
            String sql;
            int count;
            Boolean isAdmin;

            if (vulnerabilityConfig.isEnabled("SQL_INJECTION_LOGIN")) {
                sql = "SELECT COUNT(*) FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
                count = jdbcTemplate.queryForObject(sql, Integer.class);
            } else {

                sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
                count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
            }

            if (count > 0) {
                if (vulnerabilityConfig.isEnabled("SQL_INJECTION_LOGIN")) {

                    sql = "SELECT admin FROM users WHERE username = '" + username + "'";
                    isAdmin = jdbcTemplate.queryForObject(sql, Boolean.class);
                } else {
                    sql = "SELECT admin FROM users WHERE username = ?";
                    isAdmin = jdbcTemplate.queryForObject(sql, Boolean.class, username);
                }

                String token;
                if (vulnerabilityConfig.isEnabled("JWT_UNSIGNED")) {
                    token = JwtUtil.generateUnsignedToken(username, isAdmin);
                } else {
                    token = JwtUtil.generateToken(username, isAdmin);
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new AuthResponse(token));

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Invalid credentials"));
            }
        } catch (Exception e) {
            if (vulnerabilityConfig.isEnabled("STACK_TRACE")) {

                throw new RuntimeException("Test exception to verify stack trace exposure", e);
            } else {

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Internal server error"));
            }
        }    }

    public static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
