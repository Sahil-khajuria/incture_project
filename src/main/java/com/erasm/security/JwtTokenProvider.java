package com.erasm.security;
import com.erasm.config.JwtConfig; import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.slf4j.Logger; import org.slf4j.LoggerFactory; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.stereotype.Component;
import java.security.Key; import java.util.Date; import java.util.function.Function;

@Component public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final JwtConfig jwtConfig; private final Key signingKey;
    public JwtTokenProvider(JwtConfig jwtConfig) { this.jwtConfig = jwtConfig; this.signingKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()); }

    public String generateAccessToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");
        return Jwts.builder().setSubject(userDetails.getUsername()).claim("role", role).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiry())).signWith(signingKey, SignatureAlgorithm.HS256).compact();
    }
    public String generateRefreshToken(String email) { return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiry())).signWith(signingKey, SignatureAlgorithm.HS256).compact(); }
    public boolean validateToken(String token) { try { Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token); return true; } catch (Exception ex) { logger.warn("JWT validation failed: {}", ex.getMessage()); return false; } }
    public String getEmailFromToken(String token) { return extractClaim(token, Claims::getSubject); }
    public String getRoleFromToken(String token) { return extractAllClaims(token).get("role", String.class); }
    public Date getExpirationFromToken(String token) { return extractClaim(token, Claims::getExpiration); }
    public long getAccessTokenExpiry() { return jwtConfig.getAccessTokenExpiry(); }
    public long getRefreshTokenExpiry() { return jwtConfig.getRefreshTokenExpiry(); }
    private <T> T extractClaim(String token, Function<Claims, T> resolver) { return resolver.apply(extractAllClaims(token)); }
    private Claims extractAllClaims(String token) { return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody(); }
}
