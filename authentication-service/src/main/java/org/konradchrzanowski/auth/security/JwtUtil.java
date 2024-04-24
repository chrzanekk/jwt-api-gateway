package org.konradchrzanowski.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.konradchrzanowski.auth.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token) {
        var parser = Jwts.parser().verifyWith((SecretKey) key).build();
        return parser.parseSignedClaims(token).getPayload();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }



    public String generate(String userId, String role, String tokenType) {
        Map<String, String> claims = Map.of("id", userId, "role", role);
        long expMillis = "ACCESS".equalsIgnoreCase(tokenType)
                ? Long.parseLong(expiration) * 1000
                : Long.parseLong(expiration) * 1000 * 5;

        final Date now = new Date();
        final Date exp = new Date(now.getTime() + expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get("id"))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    private boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + expiration);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().decryptWith((SecretKey) key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
