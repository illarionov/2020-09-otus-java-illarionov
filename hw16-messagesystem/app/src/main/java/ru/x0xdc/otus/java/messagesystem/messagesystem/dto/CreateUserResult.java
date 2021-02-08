package ru.x0xdc.otus.java.messagesystem.messagesystem.dto;

import ru.x0xdc.otus.java.messagesystem.core.model.User;

public class CreateUserResult extends MessageHandlerResult<User> {

    private static final long serialVersionUID = 1L;

    public static CreateUserResult success(User result) {
        return new CreateUserResult(MessageHandlerStatus.SUCCESS, result, null);
    }

    public static CreateUserResult failure(Exception exception) {
        return new CreateUserResult(MessageHandlerStatus.FAILURE, null, exception);
    }

    private CreateUserResult(MessageHandlerStatus status, User result, Exception error) {
        super(status, result, error);
    }
}
