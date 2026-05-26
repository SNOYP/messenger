package com.example.messengerserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimerController {

    @GetMapping("/")
    public String checkServer() {
        return "Сервер мессенджера работает! Наша цель: 10 Января 2027 года.";
    }

}