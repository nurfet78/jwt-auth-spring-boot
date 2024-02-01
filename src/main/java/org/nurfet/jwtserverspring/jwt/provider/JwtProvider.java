package org.nurfet.jwtserverspring.jwt.provider;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.jwtserverspring.exception.CustomJwtException;
import org.nurfet.jwtserverspring.jwt.config.JwtConfig;
import org.nurfet.jwtserverspring.jwt.utils.DateUtils;
import org.nurfet.jwtserverspring.jwt.utils.JwtConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Collection;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final JwtConfig jwtConfig;
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;


    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.jwtAccessSecret = generateSecretKey(jwtConfig.getAccessSecret());
        this.jwtRefreshSecret = generateSecretKey(jwtConfig.getRefreshSecret());
    }

    private SecretKey generateSecretKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private String createToken(Map<String, Object> claims, String subject,
                               SecretKey key, int expirationMinutes) {
        LocalDateTime now = LocalDateTime.now();

        Date issuedAt = DateUtils.toDate(now);
        Date expiration = DateUtils.toDate(now.plusMinutes(expirationMinutes));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key, Jwts.SIG.HS512).compact();

    }

    public String generateToken(UserDetails user, boolean isRefreshToken) {

        Map<String, Object> claims = new HashMap<>();

        if (isRefreshToken) {
            claims.put(JwtConstants.REFRESH_TOKEN_CLAIM, true);
        } else {
            String roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            claims.put(JwtConstants.ROLES_CLAIM, roles);
        }

        int expirationMinutes = isRefreshToken ? jwtConfig.getRefreshTokenExpiration() : jwtConfig.getAccessTokenExpiration();
        SecretKey key = isRefreshToken ? jwtRefreshSecret : jwtAccessSecret;

        return createToken(claims, user.getUsername(), key, expirationMinutes);
    }

    private boolean isTokenExpired(@NonNull String token, @NonNull SecretKey secret) {
        try {
            final Date expiration = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            throw new CustomJwtException("Token expiration check error", e);
        }
    }

    private boolean validateToken(String token, SecretKey key) {
        if (isTokenExpired(token, key)) {
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new CustomJwtException("Token validation failed", e);
        }
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    private Claims extractClaims(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getAccessClaims(String token) {
        return extractClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return extractClaims(token, jwtRefreshSecret);
    }

    public Authentication getAuthentication(String token) {

        Claims claims = getAccessClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(String::trim)
                        .filter(role -> !role.isEmpty())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }
}
