package ru.otus.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.otus.core.dao.UserDao;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.DatabaseConfiguration;
import ru.otus.hibernate.HibernateSessionFactoryBuilder;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.utils.MigrationsExecutorFlyway;

@Configuration
@ComponentScan(basePackages = {"ru.otus.core", "ru.otus.hibernate", "ru.otus.utils"} )
public class RootConfig {
    
    @Bean
    DatabaseConfiguration databaseConfiguration() {
        return DatabaseConfiguration.create("/database.cfg");
    }

    @Bean(name="migrationsExecutorFlyway", initMethod = "executeMigrations")
    MigrationsExecutorFlyway MigrationsExecutorFlyway(DatabaseConfiguration databaseConfiguration) {
        return new MigrationsExecutorFlyway(databaseConfiguration);
    }

    @Bean
    @DependsOn("migrationsExecutorFlyway")
    SessionFactory sessionFactory(DatabaseConfiguration databaseConfiguration) {
        return new HibernateSessionFactoryBuilder()
                .setHibernateConfigFile("/hibernate.cfg.xml")
                .setDatabaseConfiguration(databaseConfiguration)
                .build();
    }

}
