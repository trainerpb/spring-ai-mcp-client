package com.soham.selfteaching.spring.ai;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class Test {
    @Autowired
    ChatClient chatClient;

    @org.junit.jupiter.api.Test
    void chat() {

         String message = "Hello, how are you?";

         String response = chatClient.prompt().user(message).call().content();
         System.out.println("Echo: " + response);
         assertNotNull(response);
    }
}
