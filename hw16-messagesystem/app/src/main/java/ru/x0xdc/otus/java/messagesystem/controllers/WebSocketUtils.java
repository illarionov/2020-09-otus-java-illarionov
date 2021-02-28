package ru.x0xdc.otus.java.messagesystem.controllers;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;

public final class WebSocketUtils {

    private WebSocketUtils() {
    }

    public static void convertAndSendToSessionId(SimpMessageSendingOperations messagingTemplate,
                                                 String sessionId,
                                                 String destination,
                                                 Object payload) throws MessagingException {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        messagingTemplate.convertAndSendToUser(sessionId, destination, payload, headerAccessor.getMessageHeaders());
    }
}
