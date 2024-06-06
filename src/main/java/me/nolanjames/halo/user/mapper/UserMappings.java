package me.nolanjames.halo.user.mapper;

import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.model.UserResponse;

public interface UserMappings {

    UserResponse mapToUserResponse(User user);
}
