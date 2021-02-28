package ru.x0xdc.otus.java.messagesystem.messagesystem.database;

import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.x0xdc.otus.java.messagesystem.core.model.User;
import ru.x0xdc.otus.java.messagesystem.core.service.DBServiceUser;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;
import ru.x0xdc.otus.java.messagesystem.mappers.Mapper;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.CreateUserResult;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserForm;

import java.util.Optional;

class CreateUserHandler implements RequestHandler<CreateUserResult> {

    private final DBServiceUser userService;

    private final Mapper<UserForm, UserEntity> formToEntityMapper;

    private final Mapper<User, UserEntity> userToEntityMapper;

    CreateUserHandler(DBServiceUser userService, Mapper<UserForm, UserEntity> formToEntityMapper,
                      Mapper<User, UserEntity> userToEntityMapper) {
        this.userService = userService;
        this.formToEntityMapper = formToEntityMapper;
        this.userToEntityMapper = userToEntityMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        CreateUserResult result;
        try {
            UserForm userForm = MessageHelper.getPayload(msg);
            UserEntity userEntity = formToEntityMapper.map(userForm);
            userService.saveUser(userEntity);
            User createdUser = userToEntityMapper.mapFrom(userEntity);
            result = CreateUserResult.success(createdUser);
        } catch (Exception e) {
            result = CreateUserResult.failure(e);
        }

        Message resultMessage = MessageBuilder.buildReplyMessage(msg, result);

        return Optional.of(resultMessage);
    }
}
