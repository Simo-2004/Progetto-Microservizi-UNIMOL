package it.unimol.newunimol.service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Servizio per la gestione e validazione dei token JWT (JSON Web Token).
 * Fornisce funzionalità per:
 * - Estrarre informazioni (claim) dai token,
 * - Verificare la validità di un token (scadenza e revoca),
 * - Decodificare e utilizzare una chiave pubblica RSA per la verifica della firma.
 *
 * Utilizza una chiave pubblica configurata tramite proprietà Spring (`jwt.public-key`)
 */

@Service
public class TokenJWTService {

    /**
     * Chiave pubblica in formato Base64 fornita tramite il file di configurazione `application.properties`.
     */
    @Value("${jwt.public-key}")
    private String publicKeyString;

    /**
     * Rappresentazione oggetto della chiave pubblica RSA, generata a runtime dalla stringa decodificata.
     */
    private PublicKey publicKey;

    /**
     * Insieme di token JWT invalidati (revocati). Usato per implementare una black-list temporanea.
     */
    private static final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Restituisce la chiave pubblica utilizzata per verificare la firma dei token JWT.
     * Se non è stata ancora caricata, la genera da `publicKeyString`.
     *
     * @return La chiave pubblica RSA.
     * @throws RuntimeException se si verifica un errore durante il parsing o la generazione della chiave.
     */
    private PublicKey getPublicKey() {
        if (this.publicKey == null) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(this.publicKeyString);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                this.publicKey = keyFactory.generatePublic(spec);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Chiave pubblica non è in formato Base64 valido", e);
            } catch (Exception e) {
                throw new RuntimeException("Errore pub_key, controlla application.properties", e);
            }
        }
        return publicKey;
    }

    /**
     * Estrae un singolo claim specifico da un token JWT usando la funzione {@link #extractAllClaims(String)}
     *
     * @param <T> Tipo del claim da estrarre.
     * @param token Il token JWT da cui estrarre i dati.
     * @param claimsResolver Funzione che definisce come estrarre il claim desiderato.
     * @return Il valore del claim richiesto.
     */
    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claim contenuti nel payload del token JWT.
     *
     * @param token Il token JWT da analizzare.
     * @return Un oggetto Claims contenente tutti i dati del token.
     * @throws RuntimeException se il token non è valido o non può essere parsato.
     */
    private Claims extractAllClaims (String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Estrae il nome utente (username) dal token JWT.
     *
     * @param token Il token JWT.
     * @return Il nome utente associato al token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    /**
     * Estrae il ruolo dell'utente (role) dal token JWT.
     *
     * @param token Il token JWT.
     * @return Il ruolo dell'utente (es. "ADMIN", "USER").
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Estrae la data di scadenza del token JWT.
     *
     * @param token Il token JWT.
     * @return La data/ora di scadenza del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica se il token JWT è scaduto.
     *
     * @param token Il token JWT.
     * @return
     * true se il token è scaduto.
     * false altrimenti.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Verifica la validità complessiva del token JWT.
     * Controlla sia che il token non sia scaduto, sia che non sia stato revocato (presente nella black-list).
     *
     * @param token Il token JWT da verificare.
     * @return
     * true se il token è valido.
     * false altrimenti.
     */
    public boolean isTokenValid (String token) {
        return !isTokenExpired(token) && !invalidatedTokens.contains(token);
    }

}
