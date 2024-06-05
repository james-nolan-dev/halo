package me.nolanjames.halo.security.auth.service;

import me.nolanjames.halo.security.auth.model.AuthRequest;
import me.nolanjames.halo.security.auth.model.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    AuthResponse getAccessToken(AuthRequest request);
}
