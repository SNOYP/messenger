package com.example.messengerserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring сам напишет за нас все методы вроде save(), findAll(), delete()
}