package me.nolanjames.halo.security.jwt;

import me.nolanjames.halo.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtValidationExceptionTest {
    private static JwtUtility jwtUtility;

    @BeforeAll
    static void setup() {
        jwtUtility = JwtUtility.builder()
                .issuerName("Halo")
                .secretKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz+=!")
                .accessTokenExpiration(2)
                .build();
    }

    @Test
    public void testGenerateFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = null;
            jwtUtility.generateToken(user);
        });
    }

    @Test
    public void testGenerateSuccess() {
        User user = User.builder()
                .id(1)
                .username("Jim")
                .role("Admin")
                .build();
        String token = jwtUtility.generateToken(user);

        assertThat(token).isNotNull();
    }

    @Test
    public void testValidateFail() {
        assertThrows(JwtValidationException.class, () -> {
            jwtUtility.validateToken("a.b.c");
        });
    }

    @Test
    public void testValidateSuccess() {
        User user = User.builder()
                .id(1)
                .username("Jim")
                .role("Admin")
                .build();
        String token = jwtUtility.generateToken(user);

        assertThat(token).isNotNull();
        assertDoesNotThrow(() -> {
            jwtUtility.validateToken(token);
        });
    }

}