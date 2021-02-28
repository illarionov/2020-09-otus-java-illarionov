package ru.x0xdc.otus.java.messagesystem.messagesystem.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.AppMessageType;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.CreateUserResult;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserForm;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserListResult;

import static ru.x0xdc.otus.java.messagesystem.config.MessageSystemConfig.DATABASE_MESSAGE_SERVICE_CLIENT_NAME;
import static ru.x0xdc.otus.java.messagesystem.config.MessageSystemConfig.FRONTEND_MESSAGE_SERVICE_CLIENT_QUALIFIER;

@Service
public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;

    @Autowired
    public FrontendServiceImpl(@Qualifier(FRONTEND_MESSAGE_SERVICE_CLIENT_QUALIFIER) MsClient msClient) {
        this.msClient = msClient;
    }

    @Override
    public void getAllUsers(MessageCallback<UserListResult> dataConsumer) {
        Message outMsg = msClient.produceMessage(DATABASE_MESSAGE_SERVICE_CLIENT_NAME,
                null,
                AppMessageType.GET_USER_LIST, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void createUser(UserForm request, MessageCallback<CreateUserResult> dataConsumer) {
        Message outMsg = msClient.produceMessage(DATABASE_MESSAGE_SERVICE_CLIENT_NAME,
                new UserForm(request),
                AppMessageType.CREATE_USER, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
