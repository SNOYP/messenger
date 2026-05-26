package com.example.messengerserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Автоматический поиск пользователя в базе по email
    Optional<User> findByEmail(String email);
}