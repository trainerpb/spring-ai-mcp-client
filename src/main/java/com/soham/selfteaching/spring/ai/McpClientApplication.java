package com.soham.selfteaching.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
@Slf4j
public class McpClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class,args);
    }

    @Bean
    public  ChatClient chatClient(ChatClient.Builder builder,SyncMcpToolCallbackProvider mcpToolCallbackProvider){

        return
                builder
                .defaultToolCallbacks(mcpToolCallbackProvider.getToolCallbacks())

                .build();
    }

}

