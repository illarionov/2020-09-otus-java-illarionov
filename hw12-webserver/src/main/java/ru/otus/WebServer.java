package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.DatabaseConfiguration;
import ru.otus.hibernate.HibernateSessionFactoryBuilder;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.utils.MigrationsExecutorFlyway;
import ru.otus.web.auth.UserAuthService;
import ru.otus.web.auth.UserAuthServiceImpl;
import ru.otus.web.server.UsersWebServer;
import ru.otus.web.server.UsersWebServerImpl;
import ru.otus.web.template.TemplateProcessor;
import ru.otus.web.template.TemplateProcessorImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

*/
public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private static final int WEB_SERVER_PORT = 8080;
    public static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        DatabaseConfiguration databaseConfiguration = DatabaseConfiguration.create();

        MigrationsExecutorFlyway migration = new MigrationsExecutorFlyway(databaseConfiguration);
        migration.executeMigrations();

        HibernateSessionFactoryBuilder sessionFactoryBuilder = new HibernateSessionFactoryBuilder()
                .setDatabaseConfiguration(databaseConfiguration);

        try (var sessionFactory = sessionFactoryBuilder.build()) {
            var sessionManager = new SessionManagerHibernate(sessionFactory);
            UserDao userDao = new UserDaoHibernate(sessionManager);

            DBServiceUser userService = new DbServiceUserImpl(userDao);
            TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
            UserAuthService authService = new UserAuthServiceImpl(userService);

            UsersWebServer usersWebServer = new UsersWebServerImpl(WEB_SERVER_PORT, authService, userService,
                    templateProcessor);

            usersWebServer.start();
            usersWebServer.join();
        }
    }
}
