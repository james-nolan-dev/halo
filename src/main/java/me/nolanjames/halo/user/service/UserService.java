package me.nolanjames.halo.user.service;

import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.model.UpdateUserRequest;
import me.nolanjames.halo.user.model.UserRequest;
import me.nolanjames.halo.user.model.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    List<UserResponse> listAllUsers();

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(int id, UpdateUserRequest request);
}
