package ru.x0xdc.otus.java.messagesystem.messagesystem.dto;

import ru.otus.messagesystem.message.MessageType;

public enum AppMessageType implements MessageType {
    GET_USER_LIST("getUserList"),
    CREATE_USER("createUser");

    private final String name;

    AppMessageType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
