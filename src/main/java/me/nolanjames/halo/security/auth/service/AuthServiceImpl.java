package me.nolanjames.halo.security.auth.service;

import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.security.auth.model.AuthRequest;
import me.nolanjames.halo.security.auth.model.AuthResponse;
import me.nolanjames.halo.security.service.TokenService;
import me.nolanjames.halo.user.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    @Override
    public AuthResponse getAccessToken(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return tokenService.generateToken(userDetails.getUser());
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
