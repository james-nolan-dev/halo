package me.nolanjames.halo.user.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.repository.UserRepository;
import me.nolanjames.halo.user.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user found with the given username");
        }

        return new CustomUserDetails(user.get());
    }
}
