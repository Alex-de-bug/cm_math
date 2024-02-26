package com.example.web4.repositories;

import com.example.web4.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    User findByEmail(String email);
    Optional<User> findById(Long id);
    User getUserByName(String name);
}
