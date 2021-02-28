package ru.x0xdc.otus.java.messagesystem.messagesystem.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.x0xdc.otus.java.messagesystem.config.MessageSystemConfig;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.AppMessageType;

@Component
@Qualifier(MessageSystemConfig.FRONTEND_HANDLER_STORE_QUALIFIER)
public class FrontendHandlerStore extends HandlersStoreImpl {
    @Autowired
    public FrontendHandlerStore(CallbackRegistry callbackRegistry) {
        CallbackResponseHandler callbackHandler = new CallbackResponseHandler(callbackRegistry);
        addHandler(AppMessageType.GET_USER_LIST, callbackHandler);
        addHandler(AppMessageType.CREATE_USER, callbackHandler);
    }
}
