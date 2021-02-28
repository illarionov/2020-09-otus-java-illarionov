package ru.x0xdc.otus.java.messagesystem.controllers.admin;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import ru.otus.messagesystem.client.MessageCallback;
import ru.x0xdc.otus.java.messagesystem.controllers.WebSocketUtils;
import ru.x0xdc.otus.java.messagesystem.core.dao.LoginAlreadyExistsException;
import ru.x0xdc.otus.java.messagesystem.core.model.User;
import ru.x0xdc.otus.java.messagesystem.core.service.DbServiceException;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.CreateUserResult;

import java.util.Collections;
import java.util.Map;

import static ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig.*;

public class CreateUserResponseHandler implements MessageCallback<CreateUserResult> {

    private final SimpMessageSendingOperations messagingTemplate;
    private final String sessionId;

    public CreateUserResponseHandler(SimpMessageSendingOperations messagingTemplate, String sessionId) {
        this.messagingTemplate = messagingTemplate;
        this.sessionId = sessionId;
    }

    @Override
    public void accept(CreateUserResult createUserResult) {
        if (createUserResult.isSuccess()) {
            onSuccess(createUserResult.getResult());
        } else {
            onError(createUserResult.getError());
        }
    }

    private void onSuccess(User user) {
        WebSocketUtils.convertAndSendToSessionId(messagingTemplate, sessionId, WS_DEST_CREATE_USER_UNICAST_RESPONSE,
                Map.of(
                        "message", "User created: " + user.getId(),
                        "user", user
                ));
        messagingTemplate.convertAndSend(WS_DEST_USER_CREATED_BROADCAST, user);
    }

    private void onError(Exception exception) {
        final WebSocketErrorResponse errorResponse;
        if (exception instanceof DbServiceException) {
            errorResponse = handleDbServiceException((DbServiceException) exception);
        } else {
            errorResponse = new WebSocketErrorResponse(Collections.singletonList(exception.getMessage()));
        }

        WebSocketUtils.convertAndSendToSessionId(messagingTemplate, sessionId,
                WS_DEST_ERROR_UNICAST_RESPONSE, errorResponse);
    }

    private static WebSocketErrorResponse handleDbServiceException(DbServiceException exception) {
        final String errorMessage;
        if (exception.getCause() instanceof LoginAlreadyExistsException) {
            errorMessage = "Логин уже существует";
        } else {
            errorMessage = "Ошибка создания пользователя";
        }
        return new WebSocketErrorResponse(Collections.singletonList(errorMessage));
    }
}
