package ru.otus;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.Client;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.JdbcAccountDao;
import ru.otus.jdbc.dao.JdbcClientDao;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        HikariConfig config = new HikariConfig("/hikari.properties");
        DataSource dataSource = new HikariDataSource(config);
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);
        
// Работа с пользователем
        DbExecutor dbExecutor = new DbExecutorImpl();
        JdbcMapper<Client, Long> jdbcMapperClient = JdbcMapperImpl.create(Client.class, sessionManager, dbExecutor);
        ClientDao clientDao = new JdbcClientDao(sessionManager, jdbcMapperClient);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var clientId = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 28));
        Optional<Client> clientOptional = dbServiceClient.getClient(clientId);

        clientOptional.ifPresentOrElse(
                client -> System.out.printf("Created client, name: %s\n", client.getName()),
                () -> System.out.println("Client was not created")
        );

        List<Client> clients = dbServiceClient.findAll();
        System.out.printf("Clients: %s\n", clients);

// Работа со счетом
        JdbcMapper<Account, String> jdbcMapperAccount = JdbcMapperImpl.create(Account.class, sessionManager, dbExecutor);
        AccountDao accountDao = new JdbcAccountDao(sessionManager, jdbcMapperAccount);
        
        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        var id = dbServiceAccount.saveAccount(new Account(UUID.randomUUID().toString(), "Personal", 5.6));
        Optional<Account> accountOptional = dbServiceAccount.getAccount(id);

        accountOptional.ifPresentOrElse(
                account -> System.out.printf("Created account: %s\n", account),
                () -> System.out.println("Account was not created")
        );

    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
