package me.nolanjames.halo.user.mapper;

import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMappingsImpl implements UserMappings {
    @Override
    public UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getUsername(), user.getRole());
    }
}
