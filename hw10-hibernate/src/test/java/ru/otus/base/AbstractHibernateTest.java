package ru.otus.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.core.model.User;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateSessionFactory;

@Testcontainers
public abstract class AbstractHibernateTest {

    protected SessionFactory sessionFactory;

    @Container
    private static final CustomPostgreSQLContainer CONTAINER = CustomPostgreSQLContainer.getInstance();

    @BeforeEach
    public void setUp() {
        String dbUrl = CONTAINER.getJdbcUrl();
        String dbUserName = CONTAINER.getUsername();
        String dbPassword = CONTAINER.getPassword();

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.cleanDb();
        migrationsExecutor.executeMigrations();
        
        sessionFactory = new HibernateSessionFactory()
                .setConnectionUrl(dbUrl)
                .setDbUsername(dbUserName)
                .setDbPassword(dbPassword)
                .build();
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(User.class.getName());
    }

    protected void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            saveUser(session, user);
        }
    }

    protected void saveUser(Session session, User user) {
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    protected User loadUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        }
    }
}
