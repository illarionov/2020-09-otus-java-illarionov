package ru.x0xdc.otus.java.messagesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
//@EnableScheduling
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String WEB_SOCKET_ENDPOINT = "/user-websocket";

    public static final String WS_DEST_USER_LIST_REQUEST = "/admin/users";

    public static final String WS_DEST_CREATE_USER_REQUEST = "/admin/user/create";
    public static final String WS_DEST_CREATE_USER_UNICAST_RESPONSE = "/queue/admin/user/create";

    public static final String WS_DEST_USER_LIST_BROADCAST = "/topic/admin/users";
    public static final String WS_DEST_USER_CREATED_BROADCAST = "/topic/admin/user/create";

    public static final String WS_DEST_ERROR_UNICAST_RESPONSE = "/queue/errors";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEB_SOCKET_ENDPOINT)
                .withSockJS();
    }
}