package me.nolanjames.halo.security.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.security.auth.model.AuthRequest;
import me.nolanjames.halo.security.auth.model.AuthResponse;
import me.nolanjames.halo.security.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getAccessToken(@RequestBody @Valid AuthRequest request) {

        return new ResponseEntity<>(authService.getAccessToken(request), HttpStatus.OK);
    }
}
