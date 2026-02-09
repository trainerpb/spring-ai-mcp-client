package com.soham.selfteaching.spring.ai;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpElicitation;
import org.springaicommunity.mcp.annotation.McpLogging;
import org.springaicommunity.mcp.annotation.McpProgress;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
        return
                builder
                .defaultToolCallbacks(mcpToolCallbackProvider.getToolCallbacks())
//                        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @McpProgress(clients = "ticketing-tool-streamable-v5-with-progress")
    public void progressHandler(McpSchema.ProgressNotification progressNotification) {
        log.info("MCP PROGRESS: [{}] progress: {} total: {} message: {}",
                progressNotification.progressToken(), progressNotification.progress(),
                progressNotification.total(), progressNotification.message());
    }


    @McpLogging(clients = "ticketing-tool-streamable-v5-with-progress")
    public void loggingHandler(McpSchema.LoggingMessageNotification loggingMessageNotification) {
        log.info("MCP loggingHandler:  message: {}",
                loggingMessageNotification.data());
    }


    @McpElicitation(clients = "ticketing-tool-streamable-v5-with-progress")
    public void elicitationHandler(McpSchema.ElicitRequest elicitRequest) {
        log.info("MCP elicitationHandler:  message: {}",
                elicitRequest.message());
    }
}

