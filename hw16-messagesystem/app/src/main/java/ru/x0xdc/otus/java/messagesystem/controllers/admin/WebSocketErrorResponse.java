package ru.x0xdc.otus.java.messagesystem.controllers.admin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

class WebSocketErrorResponse {

    @JsonProperty("messages")
    private final List<String> messages;

    public WebSocketErrorResponse(List<String> messages) {
        this.messages = List.copyOf(messages);
    }

    public List<String> getMessages() {
        return messages;
    }
}
