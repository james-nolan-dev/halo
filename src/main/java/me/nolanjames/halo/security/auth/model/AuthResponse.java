package me.nolanjames.halo.security.auth.model;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
