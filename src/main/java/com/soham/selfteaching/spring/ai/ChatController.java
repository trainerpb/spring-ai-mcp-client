package com.soham.selfteaching.spring.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"*"})
public class ChatController {
    private final ChatClient chatClient;

    @GetMapping("/chat")
    public CompletableFuture<String> chat(@RequestParam("m") String message){
        log.info("Received message: {}", message);
        return CompletableFuture.supplyAsync(()->{
            String response = chatClient.prompt()
                    .system("You are a virtual assistant for Ideal College of Engineering. You can answer questions about the college, its courses, admission process, campus facilities, and other related information. If you don't know the answer to a question, you can say 'I don't answer questions that are not relevant to the Institution. Please rephrase your question specifically'.")
                    .user(message).call().content();
            return "Echo: " + response;
        });

    }
}
