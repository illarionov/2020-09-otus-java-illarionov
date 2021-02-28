package ru.x0xdc.otus.java.messagesystem.messagesystem.dto;

import org.springframework.lang.Nullable;
import ru.x0xdc.otus.java.messagesystem.core.model.User;

import java.util.List;

public class UserListResult extends MessageHandlerResult<List<User>> {

    private static final long serialVersionUID = 1L;

    public static UserListResult success(List<User> result) {
        return new UserListResult(MessageHandlerStatus.SUCCESS, List.copyOf(result), null);
    }

    public static UserListResult failure(Exception exception) {
        return new UserListResult(MessageHandlerStatus.FAILURE, null, exception);
    }

    private UserListResult(MessageHandlerStatus status, @Nullable List<User> result, @Nullable Exception error) {
        super(status, result, error);
    }
}
