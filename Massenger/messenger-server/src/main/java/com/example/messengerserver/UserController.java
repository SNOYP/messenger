package com.example.messengerserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Эндпоинт для регистрации нового пользователя
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        // Проверяем, нет ли уже такого никнейма в базе
        // (Пока делаем простую реализацию, без сложной защиты)
        try {
            userRepository.save(newUser);
            return ResponseEntity.ok("Пользователь " + newUser.getUsername() + " успешно зарегистрирован!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при регистрации. Возможно, такой ник или email уже занят.");
        }
    }

    // Эндпоинт для получения списка всех пользователей (для проверки)
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}