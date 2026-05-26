package com.example.messengerserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtCore jwtCore;

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        try {
            String rawPassword = newUser.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            newUser.setPassword(encodedPassword);

            userRepository.save(newUser);

            // Сразу генерируем токен для автоматического входа после регистрации
            String token = jwtCore.generateToken(newUser.getEmail());

            // Используем put() вместо set() для работы с Map в Java
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", newUser.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при регистрации. Возможно, ник или email заняты.");
        }
    }

    // Авторизация пользователя (Вход)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Создаем JWT токен
                String token = jwtCore.generateToken(user.getEmail());

                // Возвращаем токен и имя в виде JSON-объекта, используя put()
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("username", user.getUsername());

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный email или пароль.");
    }

    // Получить список всех пользователей
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}