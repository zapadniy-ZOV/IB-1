package itmo.labs.secure_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class JwtService {

    private final Key signingKey;
    private final long accessTokenTtlSeconds;

    public JwtService(
            @Value("${JWT_SECRET:change-me-please-change-me-please-change-me}") String jwtSecret,
            @Value("${JWT_TTL_SECONDS:3600}") long accessTokenTtlSeconds) {
        this.signingKey = generateSigningKey(jwtSecret);
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
    }

    private static Key generateSigningKey(String secret) {
        // Allow both raw and base64 secrets
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException e) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessTokenTtlSeconds);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
