package me.nolanjames.halo.security.service;

import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.security.auth.model.AuthResponse;
import me.nolanjames.halo.security.jwt.JwtUtility;
import me.nolanjames.halo.user.entity.RefreshToken;
import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtility jwtUtility;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    public AuthResponse generateToken(User user) {
        String token = jwtUtility.generateToken(user);
        String randomUUID = UUID.randomUUID().toString();
        saveRefreshToken(user, randomUUID);

        return new AuthResponse(token, randomUUID);
    }

    private void saveRefreshToken(User user, String randomUUID) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(passwordEncoder.encode(randomUUID))
                .expiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration * 60000L))
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}
