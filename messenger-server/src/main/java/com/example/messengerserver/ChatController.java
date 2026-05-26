package com.example.messengerserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // Создать или получить существующий пустой чат
    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestParam String name) {
        Chat chat = new Chat();
        chat.setName(name);
        chatRepository.save(chat);
        return ResponseEntity.ok(chat);
    }

    // Отправить сообщение в чат
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestParam Long chatId, @RequestParam String email, @RequestBody String content) {
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (chatOpt.isPresent() && userOpt.isPresent()) {
            Message message = new Message();
            message.setChat(chatOpt.get());
            message.setSender(userOpt.get());
            message.setContent(content);
            messageRepository.save(message);
            return ResponseEntity.ok("Сообщение отправлено!");
        }
        return ResponseEntity.badRequest().body("Чат или пользователь не найден.");
    }

    // Получить все сообщения конкретного чата (История переписки)
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if (chatOpt.isPresent()) {
            // Для упрощения возвращаем все сообщения. На этапе WebSockets перепишем под сокеты.
            List<Message> messages = messageRepository.findAll().stream()
                    .filter(m -> m.getChat().getId().equals(chatId))
                    .toList();
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.badRequest().body("Чат не найден.");
    }
}