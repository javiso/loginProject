package com.example.project.project.repository;

import com.example.project.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(final String username);
    boolean existsByUsername(final String username);
}
