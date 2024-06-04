package me.nolanjames.halo.user.repository;

import me.nolanjames.halo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
