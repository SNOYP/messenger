package com.example.messengerserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Внедряем наш шифровщик, который мы настроили в SecurityConfig
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Эндпоинт для регистрации нового пользователя
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        try {
            // Проверяем, существует ли уже такой пользователь (опционально, но полезно)
            // Здесь можно добавить логику проверки по username или email

            // 1. Берем пароль из входящего JSON
            String rawPassword = newUser.getPassword();

            // 2. Шифруем его с помощью BCrypt
            String encodedPassword = passwordEncoder.encode(rawPassword);

            // 3. Устанавливаем зашифрованный пароль обратно в объект
            newUser.setPassword(encodedPassword);

            // 4. Сохраняем пользователя в базу
            userRepository.save(newUser);

            return ResponseEntity.ok("Пользователь " + newUser.getUsername() + " успешно зарегистрирован!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при регистрации: " + e.getMessage());
        }
    }

    // Эндпоинт для получения списка всех пользователей
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}