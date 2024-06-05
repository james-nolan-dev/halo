package me.nolanjames.halo.user.service;

import me.nolanjames.halo.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
