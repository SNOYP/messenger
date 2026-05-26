package com.example.messengerserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        try {
            String rawPassword = newUser.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            newUser.setPassword(encodedPassword);

            userRepository.save(newUser);
            return ResponseEntity.ok("Пользователь " + newUser.getUsername() + " успешно зарегистрирован!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при регистрации. Возможно, такой ник или email уже занят.");
        }
    }

    // Авторизация пользователя (Вход)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginRequest) {
        // Ищем пользователя по email
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Сверяем введенный сырой пароль с зашифрованным в БД
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok("Успешный вход! Добро пожаловать, " + user.getUsername());
            }
        }
        // Если пользователь не найден или пароль не совпал
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный email или пароль.");
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}