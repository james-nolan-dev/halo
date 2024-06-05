package me.nolanjames.halo.user.service;

import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);

    }
}
