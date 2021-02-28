package ru.x0xdc.otus.java.messagesystem.messagesystem.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

class CallbackResponseHandler implements RequestHandler<ResultDataType> {

    private static final Logger logger = LoggerFactory.getLogger(CallbackResponseHandler.class);

    private final CallbackRegistry callbackRegistry;

    public CallbackResponseHandler(CallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        MessageCallback<?> resultCallback = callbackRegistry.getAndRemove(msg.getCallbackId());
        if (resultCallback != null) {
            try {
                resultCallback.accept(MessageHelper.getPayload(msg));
            } catch (Exception e) {
                logger.error("Failed to execute callback", e);
            }
        }
        return Optional.empty();
    }
}
