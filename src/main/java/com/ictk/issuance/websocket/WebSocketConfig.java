package com.ictk.issuance.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.workstatus.WebSocketRequestDTO;
import com.ictk.issuance.data.dto.workstatus.WebSocketResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler() {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        // Handle connection setup
                        log.debug("Connection established with session ID: {}", session.getId());
                        session.sendMessage(new TextMessage("connected"));
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                        try {
                            log.debug("Received message: {}", message.getPayload());

                            // Parse the JSON message into WebSocketRequestDTO
                            ObjectMapper objectMapper = new ObjectMapper();
                            WebSocketRequestDTO.WebSocketRequest request = objectMapper.readValue(
                                    message.getPayload().toString(),
                                    WebSocketRequestDTO.WebSocketRequest.class
                            );

                            // Extract header and body from the request
                            WebSocketRequestDTO.WebSocketRequestHeader header = request.getHeader();
                            WebSocketRequestDTO.WebSocketRequestBody body = request.getBody();

                            // Create an instance of WebSocketService
                            WebSocketService webSocketService = new WebSocketService();

                            // Process the request using WebSocketService
                            WebSocketResponseDTO.WebSocketResponse response = webSocketService.processRequest(header, body);

                            // Convert the response into JSON
                            String responseJson = objectMapper.writeValueAsString(response);

                            // Send the response back to the client
                            session.sendMessage(new TextMessage(responseJson));

                        } catch (Exception e) {
                            log.error("Error processing WebSocket message", e);
                            session.sendMessage(new TextMessage("Error processing request: " + e.getMessage()));
                        } finally {
                            // Close the session after sending the response
                            session.close(CloseStatus.NORMAL);
                        }
                    }

                    @Override
                    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                        // Handle any transport error
                        log.debug("Handle any transport error");
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        // Handle connection closure
                        log.debug("Handle connection closure");
                    }

                    @Override
                    public boolean supportsPartialMessages() {
                        return false;
                    }
                }, "/ws/work/{trId}")  // Match the URL with trId as a path variable
                .setAllowedOrigins("*");  // Allow front-end URLs
    }

    // ServerEndpointExporter to handle @ServerEndpoint annotations
//    @Bean
//    @Lazy // Introduce lazy initialization for serverEndpointExporter
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}
