package com.medical.auth_service.controller;

import com.medical.auth_service.service.BlacklistService;
import com.medical.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final BlacklistService blacklistService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        blacklistService.addToBlacklist(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        if (blacklistService.isBlacklisted(token)) {
            return ResponseEntity.status(401).body("Token is blacklisted!");
        }

        if (jwtUtil.isTokenValid(token)) {
            return ResponseEntity.ok("Token is valid!");
        }

        return ResponseEntity.status(401).body("Token is invalid!");
    }
}
