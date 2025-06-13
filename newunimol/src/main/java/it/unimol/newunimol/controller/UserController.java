package it.unimol.newunimol.controller;

import it.unimol.newunimol.service.TokenJWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final TokenJWTService tokenJWTService;

    public UserController(TokenJWTService tokenJWTService) {
        this.tokenJWTService = tokenJWTService;
    }

    @GetMapping("/api/v1/auth/me")
    public ResponseEntity<Map<String, Object>> getUserInfo(
            HttpServletRequest request) {

        if (request == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Token non fornito"));
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", ""); //rimuove bearer
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isValid = tokenJWTService.isTokenValid(token);
            if (!isValid) {
                response.put("error", "Token non valido");
                return ResponseEntity.status(401).body(response);
            }

            response.put("username", tokenJWTService.extractUsername(token));
            response.put("role", tokenJWTService.extractRole(token));
            response.put("valid", tokenJWTService.isTokenValid(token));
            response.put("expired", tokenJWTService.isTokenExpired(token));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Errore nella verifica del token");
            return ResponseEntity.status(401).body(response);
        }
    }
}