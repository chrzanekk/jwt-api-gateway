package org.konradchrzanowski.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.konradchrzanowski.auth.security.services.UserDetailsImpl;
import org.konradchrzanowski.utils.enumerated.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String LOGIN = "login";
    private static final String TYPE = "type";

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

    public String generateJwtToken(Authentication authentication, TokenType tokenType) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Date validityTo = generateExpireDate(tokenType);
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authentication.getAuthorities());
        claims.put(LOGIN, userDetails.getUsername());
        claims.put(TYPE, tokenType.toString());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(validityTo)
                .signWith(key).compact();
    }

    private Date generateExpireDate(TokenType tokenType) {
        long tokenExpirationDateByType = generateExpireDateByTokenType(tokenType);
        long now = (new Date()).getTime();
        return new Date(now + tokenExpirationDateByType);
    }

    private long generateExpireDateByTokenType(TokenType tokenType) {
        if (tokenType.equals(TokenType.ACCESS)) {
            return Long.parseLong(expiration);
        } else {
            return Long.parseLong(expiration) * 5;
        }
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().decryptWith((SecretKey) key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
