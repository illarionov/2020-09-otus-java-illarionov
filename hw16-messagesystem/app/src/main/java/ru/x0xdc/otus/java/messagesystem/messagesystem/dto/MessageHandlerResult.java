package ru.x0xdc.otus.java.messagesystem.messagesystem.dto;

import org.springframework.lang.Nullable;
import ru.otus.messagesystem.client.ResultDataType;

public abstract class MessageHandlerResult<T> extends ResultDataType {

    private static final long serialVersionUID = 1L;

    private final MessageHandlerStatus status;

    @Nullable
    private final T result;

    @Nullable
    private final Exception error;

    MessageHandlerResult(MessageHandlerStatus status, @Nullable T result, @Nullable Exception error) {
        this.status = status;
        this.result = result;
        this.error = error;
    }

    public MessageHandlerStatus getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return getStatus() == MessageHandlerStatus.SUCCESS;
    }

    @Nullable
    public T getResult() {
        return result;
    }

    @Nullable
    public Exception getError() {
        return error;
    }

}
