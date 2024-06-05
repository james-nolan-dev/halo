package me.nolanjames.halo.security.auth.model;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AuthRequest(
        @NotNull
        @Length(min = 3, max = 30)
        String username,
        @NotNull
        @Length(min = 3, max = 80)
        String password
) {
}
