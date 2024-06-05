package me.nolanjames.halo.user.repository;

import me.nolanjames.halo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testAddFirstUser() {
        String rawPassword = "mij";
        User user1 = User.builder()
                .username("Jim")
                .role("admin")
                .password(passwordEncoder.encode(rawPassword))
                .build();

        User savedUser = userRepository.save(user1);

        assertThat(savedUser).isNotNull();
    }

    @Test
    public void testAddSecondUser() {
        String rawPassword = "mij";
        User user2 = User.builder()
                .username("handler")
                .role("handler")
                .password(passwordEncoder.encode(rawPassword))
                .build();

        User savedUser = userRepository.save(user2);

        assertThat(savedUser).isNotNull();
    }

    @Test
    public void testFindUserNotFound() {
        Optional<User> user = userRepository.findByUsername("xxxxx");

        assertThat(user).isNotPresent();
    }

    @Test
    public void testFindUserFound() {
        Optional<User> user = userRepository.findByUsername("handler");

        assertThat(user).isPresent();
        User user2 = user.get();
        assertThat(user2.getUsername()).isEqualTo("handler");
    }

}