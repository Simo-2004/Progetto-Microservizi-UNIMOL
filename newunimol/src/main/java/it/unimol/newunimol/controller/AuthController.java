//stub per la generazione del token
package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Value("${jwt.private-key}")
    private String privateKeyString;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostMapping("/api/v1/token/generate")
    public ResponseEntity<Map<String, String>> generateToken(@RequestBody LoginRequest loginRequest) {
        try {
            // Decodifica della chiave privata
            PrivateKey privateKey = getPrivateKey();

            // Costruzione del token
            String token = Jwts.builder()
                    .setSubject("user123") // es. userId
                    .claim("username", loginRequest.getUsername())
                    .claim("role", "ADMIN") //creazione ruolo
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nella generazione del token");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private PrivateKey getPrivateKey() throws Exception {
        String privateKeyPEM = privateKeyString
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

}