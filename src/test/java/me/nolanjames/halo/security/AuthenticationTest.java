package me.nolanjames.halo.security;

import me.nolanjames.halo.user.security.CustomUserDetails;
import me.nolanjames.halo.user.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthenticationTest {
    @Autowired
    AuthenticationManager authenticationManager;

    @Test
    public void testAuthenticationFail() {
        assertThrows(BadCredentialsException.class, () -> {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("jim", "xxx"));
        });
    }

    @Test
    public void testAuthenticationSuccess() {
        String username = "Jim";
        String password = "mij";
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        assertThat(authentication.isAuthenticated()).isTrue();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        assertThat(userDetails.getUsername()).isEqualTo(username);

    }
}
