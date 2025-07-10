package ru.vsu.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.dsr.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
}
