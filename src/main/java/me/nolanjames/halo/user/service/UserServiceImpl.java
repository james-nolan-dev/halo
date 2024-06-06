package me.nolanjames.halo.user.service;

import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.mapper.UserMappings;
import me.nolanjames.halo.user.mapper.UserMappingsImpl;
import me.nolanjames.halo.user.model.UserRequest;
import me.nolanjames.halo.user.model.UserResponse;
import me.nolanjames.halo.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMappings userMappings;
    private final UserMappingsImpl userMappingsImpl;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);

    }

    @Override
    public List<UserResponse> listAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMappings::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        LOGGER.info("User request: {}", request.username());
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role("user")
                .build();

        userRepository.save(user);

        return userMappingsImpl.mapToUserResponse(user);
    }
}
