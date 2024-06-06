package me.nolanjames.halo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import me.nolanjames.halo.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUtility {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final String SECRET_KEY_ALGORITHM = "HmacSHA512";

    @Value("${app.security.jwt.issuer}")
    private String issuerName;
    @Value("${app.security.jwt.secret}")
    private String secretKey;
    @Value("${app.security.jwt.access-token.expiration}")
    private int accessTokenExpiration;

    public String generateToken(User user) {
        if (user == null || user.getId() == null || user.getUsername() == null || user.getRole() == null) {
            throw new IllegalArgumentException("User object is null or its field have null values.");
        }
        long expirationTimeInMillis = System.currentTimeMillis() + accessTokenExpiration * 60000L;
        String subject = String.format("%s,%s", user.getId(), user.getUsername());

        return Jwts.builder()
                .subject(subject)
                .issuer(issuerName)
                .issuedAt(new Date())
                .expiration(new Date(expirationTimeInMillis))
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS512)
                .compact();
    }

    public Claims validateToken(String token) throws JwtValidationException {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), SECRET_KEY_ALGORITHM);

            return Jwts.parser()
                    .verifyWith(keySpec)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException exception) {
            throw new JwtValidationException("Access token expired", exception);
        } catch (IllegalArgumentException exception) {
            throw new JwtValidationException("Access token is illegal", exception);
        } catch (MalformedJwtException exception) {
            throw new JwtValidationException("Access token is not well formed", exception);
        } catch (UnsupportedJwtException exception) {
            throw new JwtValidationException("Access token is not supported", exception);
        }
    }
}
