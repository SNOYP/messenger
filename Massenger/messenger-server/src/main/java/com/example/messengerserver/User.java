package com.example.messengerserver;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Так таблица будет называться в базе данных
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный номер пользователя

    @Column(nullable = false, unique = true)
    private String username; // Никнейм (обязательный и уникальный)

    @Column(nullable = false, unique = true)
    private String email; // Почта

    @Column(nullable = false)
    private String password; // Пароль

    // Пустой конструктор (нужен для Spring)
    public User() {
    }

    // --- Ниже идут Геттеры и Сеттеры ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}