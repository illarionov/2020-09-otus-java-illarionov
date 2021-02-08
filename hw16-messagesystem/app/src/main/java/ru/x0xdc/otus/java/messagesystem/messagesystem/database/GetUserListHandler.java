package ru.x0xdc.otus.java.messagesystem.messagesystem.database;

import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.x0xdc.otus.java.messagesystem.core.model.User;
import ru.x0xdc.otus.java.messagesystem.core.service.DBServiceUser;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;
import ru.x0xdc.otus.java.messagesystem.mappers.Mapper;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserListResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class GetUserListHandler implements RequestHandler<UserListResult> {

    private final DBServiceUser userService;

    private final Mapper<User, UserEntity> userToUserEntityMapper;

    GetUserListHandler(DBServiceUser userService, Mapper<User, UserEntity> userToUserEntityMapper) {
        this.userService = userService;
        this.userToUserEntityMapper = userToUserEntityMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserListResult result;
        try {
            List<UserEntity> userEntities = userService.findAllUsers();
            List<User> users = userEntities
                    .stream()
                    .map(userToUserEntityMapper::mapFrom)
                    .collect(Collectors.toUnmodifiableList());
            result = UserListResult.success(users);
        } catch (Exception e) {
            result = UserListResult.failure(e);
        }

        Message resultMessage = MessageBuilder.buildReplyMessage(msg, result);
        return Optional.of(resultMessage);
    }
}
