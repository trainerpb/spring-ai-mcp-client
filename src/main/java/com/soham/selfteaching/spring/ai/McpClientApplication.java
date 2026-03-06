package com.soham.selfteaching.spring.ai;

import io.modelcontextprotocol.client.McpSyncClient;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

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

    @Bean
    public CommandLineRunner commandLineRunner(SyncMcpToolCallbackProvider mcpSyncClient){
        return args -> {
            log.info("MCP Client Application started successfully.");
            Arrays.stream(mcpSyncClient.getToolCallbacks()).forEach(tc->{

                log.info("Tool Callback FQCN: {} , {} {} {} ", tc.getClass().getName(),tc.getToolDefinition().description(),tc.getToolDefinition().name(),tc.getToolDefinition().inputSchema());
            });
        };
    }


    @McpElicitation(clients = "spring-ai-mcp-one")
    public McpSchema.ElicitResult handleRequest(McpSchema.ElicitRequest request) {
        // 1. Show a Swing/JavaFX dialog, a CLI prompt, or a Web Socket message
        System.out.println("SERVER ASKS: " + request.message());

        // 2. Gather data (example using Scanner for CLI)
        Scanner scanner = new Scanner(System.in);
        String confirmOrder = scanner.nextLine();

        // 3. Return the data back to the server
        Map<String, Object> data = Map.of("confirmOrder", confirmOrder);
        log.info("Sending elicited data back to server: {}", data);
        return new McpSchema.ElicitResult(McpSchema.ElicitResult.Action.ACCEPT, data);
    }
}

