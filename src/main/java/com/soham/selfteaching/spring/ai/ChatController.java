package com.soham.selfteaching.spring.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j

public class ChatController {
    private final ChatClient chatClient;

    @GetMapping("/chat")
    public CompletableFuture<String> chat(@RequestParam("m") String message){
        log.info("Received message: {}", message);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            String response = chatClient.prompt().user(message).call().content();
            return "Echo: " + response;
        });
        return future;

    }
}
