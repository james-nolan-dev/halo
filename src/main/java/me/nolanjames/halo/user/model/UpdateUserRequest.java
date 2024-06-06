package me.nolanjames.halo.user.model;

public record UpdateUserRequest(
        String username,
        String password,
        String role
) {
}
