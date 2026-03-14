package com.soham.selfteaching.spring.ai;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpElicitation;
import org.springaicommunity.mcp.context.StructuredElicitResult;
import org.springframework.ai.document.id.RandomIdGenerator;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.random.RandomGenerator;

/**
 * This class controls the HITL elcit req-response cycle
 *
 * @author : Soham Sengupta
 */
@Component
@RestController
@CrossOrigin(origins = {"*"})
@Slf4j
@RequiredArgsConstructor
public class ElicitationHandler {

    private final Map<String, CompletableFuture<StructuredElicitResult>> elicitations = new ConcurrentHashMap<>();

    private final SimpMessagingTemplate messagingTemplate;


    /**
     * We cannot use console based Elicit interaction in HITL flow
     * For web based users, we need HTTP based interaction
     *
     * @param request
     * @return a future containing elicitResult CompletableFuture<McpSchema.ElicitResult>
     */
    @McpElicitation(clients = "spring-ai-mcp-one")
    public  StructuredElicitResult<Map<String,Object>> handleRequest(McpSchema.ElicitRequest request) {
        // 1. Show a Swing/JavaFX dialog, a CLI prompt, or a Web Socket message
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  SERVER ASKS: {}", request.message());
        String confirmOrder = null;
        var r= getResult(request);
        elicitations.remove(r.structuredContent().get("id").toString());
        return r;

    }

    private StructuredElicitResult<Map<String, Object>> getResult(McpSchema.ElicitRequest request) {
        String requestId =""+ Objects.hash(UUID.randomUUID().toString()); //generate a requestId

        CompletableFuture<StructuredElicitResult> future = CompletableFuture.supplyAsync(
                ()->{
                    var elicitRequest = Map.of(
                            "id", requestId,
                            "message", request.message(),
                            "schema", request.requestedSchema()
                    );
                    sendRequestMessageToWebUser(elicitRequest); // we use this method to encapsulate send logic
                    try {
                        TimeUnit.MINUTES.sleep(2L);
                    } catch (InterruptedException e) {
                        log.error("Error while sleeping", e );
                    }
                    return StructuredElicitResult.builder().action(McpSchema.ElicitResult.Action.CANCEL)
                            .structuredContent(null)
                            .build()
                            ;
                }


        ); // future result
        elicitations.put(requestId, future); // adding this future for In-memory queue

        try {
            return future.get(2L,TimeUnit.MINUTES);
        }catch (InterruptedException|TimeoutException|ExecutionException e) {
            log.error("Elicitation request failed.", e.fillInStackTrace());

           return  StructuredElicitResult.builder().action(McpSchema.ElicitResult.Action.CANCEL)
                   .structuredContent(Map.<String,Object>of())
                   .build();

        }

    }

    /**
     * This method is used as an abstraction to sending
     * Elicitation Request to the user, it maybe anything, from WebSocket to pub-sub events, SSE, etc.
     * Even you could use it to provide console output 😎
     *
     * @param elicitRequest
     */
    private void sendRequestMessageToWebUser(Map<String, Object> elicitRequest) {

        log.info("<<<<<<<<<<<<<<<<<<        Elicitation request was received : {} ##################", elicitRequest.toString());
        log.info("Pushing Elicitation to WebSocket: {}", elicitRequest.get("id"));

        // This sends the payload to all clients subscribed to /topic/elicitation
        messagingTemplate.convertAndSend("/topic/elicitation", elicitRequest);
    }

    /**
     * Called when user clicks 'Submit' on the Web UI
     */

    @PostMapping("/elicit-response")
    public void onUserResponse(@RequestParam Map<String, Object> payload) {
        String id = (String) payload.get("id"); // Request Id
        String confirmOrder = (String) payload.get("confirmOrder");

        if (elicitations.containsKey(id)) {
            elicitations.get(id).complete(
                    StructuredElicitResult
                            .builder().
                            action
                            ("yes".equalsIgnoreCase(confirmOrder)? McpSchema.ElicitResult.Action.ACCEPT: McpSchema.ElicitResult.Action.DECLINE)
                                    .structuredContent(payload)
                                            .build());

            elicitations.remove(id);
        }
    }
}
