package ru.x0xdc.otus.java.messagesystem.controllers.admin;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import ru.otus.messagesystem.client.MessageCallback;
import ru.x0xdc.otus.java.messagesystem.controllers.WebSocketUtils;
import ru.x0xdc.otus.java.messagesystem.core.model.User;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserListResult;

import java.util.List;

import static java.util.Collections.singletonList;
import static ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig.WS_DEST_ERROR_UNICAST_RESPONSE;
import static ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig.WS_DEST_USER_LIST_BROADCAST;

class GetUserListResponseHandler implements MessageCallback<UserListResult> {

    private final SimpMessageSendingOperations messagingTemplate;

    private final String sessionId;

    GetUserListResponseHandler(SimpMessageSendingOperations messagingTemplate, String sessionId) {
        this.messagingTemplate = messagingTemplate;
        this.sessionId = sessionId;
    }

    @Override
    public void accept(UserListResult userListResult) {
        if (userListResult.isSuccess()) {
            onSuccess(userListResult.getResult());
        } else {
            onError(userListResult.getError());
        }
    }

    private void onSuccess(List<User> users) {
        messagingTemplate.convertAndSend(WS_DEST_USER_LIST_BROADCAST, users);
    }

    private void onError(Exception exception) {
        WebSocketErrorResponse errorResponse = new WebSocketErrorResponse(singletonList(exception.getMessage()));
        WebSocketUtils.convertAndSendToSessionId(messagingTemplate, sessionId,
                WS_DEST_ERROR_UNICAST_RESPONSE, errorResponse);
    }
}
