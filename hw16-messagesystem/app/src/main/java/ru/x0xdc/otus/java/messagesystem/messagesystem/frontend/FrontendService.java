package ru.x0xdc.otus.java.messagesystem.messagesystem.frontend;

import ru.otus.messagesystem.client.MessageCallback;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.CreateUserResult;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserForm;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserListResult;

public interface FrontendService {
    void getAllUsers(MessageCallback<UserListResult> dataConsumer);

    void createUser(UserForm request, MessageCallback<CreateUserResult> dataConsumer);
}
