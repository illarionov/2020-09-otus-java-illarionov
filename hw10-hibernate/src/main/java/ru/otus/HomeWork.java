package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateSessionFactory;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        try (var sessionFactory = new HibernateSessionFactory().build()) {
            var sessionManager = new SessionManagerHibernate(sessionFactory);

            // Работа с пользователем
            UserDao userDao = new UserDaoHibernate(sessionManager);
            DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

            User user = User.builder()
                    .setName("Вася")
                    .setAge(55)
                    .setAddress("ул. Михайлова")
                    .addPhone("11111")
                    .addPhone("22222")
                    .addPhone("33333")
                    .build();

            long id = dbServiceUser.saveUser(user);
            Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);
            mayBeCreatedUser.ifPresentOrElse((usr) -> outputUser("Created user", usr),
                    () -> logger.info("User not found"));


            User modifiedUser = User.builder()
                    .setId(id)
                    .setName("Не вася")
                    .setAge(20)
                    .setAddress("ул. Петрова")
                    .addPhone("33333")
                    .addPhone("44444")
                    .build();

            id = dbServiceUser.saveUser(modifiedUser);
            Optional<User> mayBeUpdatedUser = dbServiceUser.getUser(id);
            mayBeUpdatedUser.ifPresentOrElse((user1) -> outputUser("Updated user", user1),
                    () -> logger.info("User not found"));

        }
    }

    private static void outputUser(String header, User user) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("user:{}", user);
    }
}
