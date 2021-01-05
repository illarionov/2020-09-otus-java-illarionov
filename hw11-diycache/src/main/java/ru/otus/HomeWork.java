package ru.otus;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.Client;
import ru.otus.core.service.*;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.JdbcAccountDao;
import ru.otus.jdbc.dao.JdbcClientDao;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    final HwCache<String, Client> clientCache;

    final HwCache<String, Account> accountCache;

    final HwListener<String, Object> loggerListener;

    final DataSource dataSource;

    final DBServiceClient dbServiceClient;

    final DbServiceAccount nonCachedServiceAccount;

    final DbServiceAccount cachedServiceAccount;

    public static void main(String[] args) {
        new HomeWork().start();
    }

    public HomeWork() {
        clientCache = new MyCache<>();
        accountCache = new MyCache<>();

        loggerListener = new HwListener<>() {
            @Override
            public void notify(String key, Object value, String action) {
                logger.info("Cache event: `{}`; key: `{}`, value: `{}`", action, key, value);
            }
        };

        clientCache.addListener(loggerListener);
        accountCache.addListener(loggerListener);

        HikariConfig config = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(config);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);

        DbExecutor dbExecutor = new DbExecutorImpl();
        JdbcMapper<Client, Long> jdbcMapperClient = JdbcMapperImpl.create(Client.class, sessionManager, dbExecutor);
        JdbcMapper<Account, String> jdbcMapperAccount = JdbcMapperImpl.create(Account.class, sessionManager, dbExecutor);

        ClientDao clientDao = new JdbcClientDao(sessionManager, jdbcMapperClient);
        AccountDao accountDao = new JdbcAccountDao(sessionManager, jdbcMapperAccount);

        dbServiceClient = new CachedDbServiceClientImpl(clientDao, clientCache);

        nonCachedServiceAccount = new DbServiceAccountImpl(accountDao);
        cachedServiceAccount = new CachedDbServiceAccountImpl(nonCachedServiceAccount, accountCache);
    }

    private void start() {
        // Общая часть
        flywayMigrations();

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var clientId = dbServiceClient.saveClient(new Client(0, "Олег", 28));
        Optional<Client> clientOptional = dbServiceClient.getClient(clientId);

        clientOptional.ifPresentOrElse(
                client -> System.out.printf("Created client, name: %s\n", client.getName()),
                () -> System.out.println("Client was not created")
        );

        List<Client> clients = dbServiceClient.findAll();
        System.out.printf("Clients: %s\n", clients);

// Работа со счетом
        var id = cachedServiceAccount.saveAccount(new Account(UUID.randomUUID().toString(), "Personal", 5.6));
        Optional<Account> accountOptional = cachedServiceAccount.getAccount(id);

        accountOptional.ifPresentOrElse(
                account -> System.out.printf("Created account: %s\n", account),
                () -> System.out.println("Account was not created")
        );

        testCachePerformance();
        testCacheOnOutOfMemory();
    }

    private void testCachePerformance() {
        Random random = new Random(42);
        accountCache.removeListener(loggerListener);

        List<Account> randomAccounts = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            Account randomAccount = new Account(UUID.randomUUID().toString(),
                    random.nextBoolean() ? "Personal" : "Business",
                    Math.floor((random.nextDouble() * 10000) * 100) / 100);
            randomAccounts.add(randomAccount);
        }

        for (Account account: randomAccounts) {
            cachedServiceAccount.saveAccount(account);
        }

        Collections.shuffle(randomAccounts);

        long timeWithCache = measureTimOfLoadingAccounts(randomAccounts, cachedServiceAccount);
        long timeWithoutCache = measureTimOfLoadingAccounts(randomAccounts, nonCachedServiceAccount);

        logger.info("Loading accounts. With cache: {} ms. Without cache: {} ms",
                TimeUnit.NANOSECONDS.toMillis(timeWithCache),
                TimeUnit.NANOSECONDS.toMillis(timeWithoutCache));
    }

    private void testCacheOnOutOfMemory() {
        final int[] accountAddedTimes = {0};
        HwListener<String, Account> listener = new HwListener<>() {
            @Override
            public void notify(String key, Account value, String action) {
                if (MyCache.ACTION_ELEMENT_ADDED.equals(action)) {
                    logger.info("Account {} ADDED to cache", key);
                    accountAddedTimes[0] += 1;
                }
            }
        };
        accountCache.addListener(listener);

        Account randomAccount = new Account(UUID.randomUUID().toString(), "Personal", 50);

        logger.info("**Saving new account**");
        String id = cachedServiceAccount.saveAccount(randomAccount);

        System.gc();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // ignore
        }

        logger.info("**Loading account after GC**");
        Optional<Account> afterGc = cachedServiceAccount.getAccount(id);
        
        logger.info("Account added to cache {} times", accountAddedTimes[0]);
        logger.info("Account after GC: {}", afterGc.orElse(null));
    }

    private long measureTimOfLoadingAccounts(List<Account> accounts, DbServiceAccount service) {
        long startTime = System.nanoTime();
        long found = 0;
        for (Account a: accounts) {
            Optional<Account> fromDb = service.getAccount(a.getNo());
            if (fromDb.isPresent()) found += 1;
        }
        long elapsedTime = System.nanoTime() - startTime;
        if (found != accounts.size()) throw new IllegalStateException("Found accounts: " + found);
        return elapsedTime;
    }

    private void flywayMigrations() {
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
