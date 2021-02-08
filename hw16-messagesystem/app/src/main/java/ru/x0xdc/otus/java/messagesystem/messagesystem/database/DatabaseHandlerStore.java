package ru.x0xdc.otus.java.messagesystem.messagesystem.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.x0xdc.otus.java.messagesystem.config.MessageSystemConfig;
import ru.x0xdc.otus.java.messagesystem.core.model.User;
import ru.x0xdc.otus.java.messagesystem.core.service.DBServiceUser;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;
import ru.x0xdc.otus.java.messagesystem.mappers.Mapper;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.AppMessageType;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserForm;

@Component
@Qualifier(MessageSystemConfig.DATABASE_HANDLER_STORE_QUALIFIER)
public class DatabaseHandlerStore extends HandlersStoreImpl {

    @Autowired
    public DatabaseHandlerStore(DBServiceUser userService,
                                Mapper<User, UserEntity> userToUserEntityMapper,
                                Mapper<UserForm, UserEntity> userFormToUserEntityMapper) {
        addHandler(AppMessageType.GET_USER_LIST, new GetUserListHandler(userService, userToUserEntityMapper));
        addHandler(AppMessageType.CREATE_USER, new CreateUserHandler(userService, userFormToUserEntityMapper, userToUserEntityMapper));
    }
}
