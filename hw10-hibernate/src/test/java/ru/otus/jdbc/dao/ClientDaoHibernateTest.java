package ru.otus.jdbc.dao;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с клиентами должно ")
class ClientDaoHibernateTest extends AbstractHibernateTest {

    private SessionManagerHibernate sessionManagerHibernate;
    private UserDaoHibernate clientDaoHibernate;

    Logger sqlLogger;
    ListAppender<ILoggingEvent> sqlLog = new ListAppender<>();

    @BeforeEach
    @Override
    public void setUp() {
        sqlLogger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");
        sqlLogger.setLevel(Level.DEBUG);
        sqlLogger.addAppender(sqlLog);
        sqlLog.start();

        super.setUp();
        
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        clientDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
    }

    @AfterEach
    public void tearDown() {
        sqlLogger.detachAppender(sqlLog);
        sqlLog.list.clear();
    }

    @Test
    @DisplayName(" корректно загружать клиента по заданному id")
    void shouldFindCorrectClientById() {
        User expectedClient = User.builder()
                .setName("Вася")
                .setAge(18)
                .setAddress("ул. Зеленая")
                .build();
        saveUser(expectedClient);

        assertThat(expectedClient.getId()).isPositive();

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeClient = clientDaoHibernate.findById(expectedClient.getId());
        sessionManagerHibernate.commitSession();

        assertThat(mayBeClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedClient);
    }

    @DisplayName(" корректно сохранять клиента")
    @Test
    void shouldCorrectSaveClient() {
        User expectedClient = User.builder()
                .setName("Вася")
                .setAge(20)
                .setAddress("ул. Зеленая")
                .addPhone("1111").addPhone("2222").addPhone("3333")
                .build();

        sessionManagerHibernate.beginSession();
        clientDaoHibernate.insert(expectedClient);
        long id = expectedClient.getId();
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);

        User actualClient = loadUser(id);
        assertThat(actualClient).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("address", "phones")
                .isEqualTo(expectedClient);
        assertThat(actualClient.getAddress().getStreet())
                .isEqualTo(expectedClient.getAddress().getStreet());
        assertThat(actualClient.getPhones())
                .flatExtracting(Phone::getNumber)
                .containsExactlyInAnyOrder(expectedClient.getPhones().stream().map(Phone::getNumber).toArray());

        expectedClient = User.builder()
                .setId(id)
                .setName("Не Вася")
                .setAge(30)
                .setAddress("пр. Энергетиков")
                .addPhone("2222").addPhone("6666")
                .build();

        sessionManagerHibernate.beginSession();
        clientDaoHibernate.update(expectedClient);
        long newId = expectedClient.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualClient = loadUser(newId);
        assertThat(actualClient).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("address", "phones")
                .isEqualTo(expectedClient);
        assertThat(actualClient.getAddress().getStreet())
                .isEqualTo(expectedClient.getAddress().getStreet());
        assertThat(actualClient.getPhones())
                .flatExtracting(Phone::getNumber)
                .containsExactlyInAnyOrder(expectedClient.getPhones().stream().map(Phone::getNumber).toArray());
    }

    @Test
    @DisplayName("  должен создать только три таблицы: для телефонов, адресов и пользователей")
    void shouldCreateOnlyRequiredTables() {
        sessionManagerHibernate.beginSession();
        sessionManagerHibernate.commitSession();

        assertThat(sqlLog.list)
                .extracting(ILoggingEvent::getMessage)
                .filteredOn(message -> message.contains("create table"))
                .hasSize(3);
    }

    @Test
    @DisplayName(" при сохранении клиента не использовать UPDATE")
    void shouldNotUseUpdateOnNewEntry() {
        sqlLog.list.clear();

        User expectedClient = User.builder()
                .setName("Вася")
                .setAge(20)
                .setAddress("ул. Зеленая")
                .addPhone("1111").addPhone("2222").addPhone("3333")
                .build();

        sessionManagerHibernate.beginSession();
        clientDaoHibernate.insert(expectedClient);
        sessionManagerHibernate.commitSession();

        assertThat(sqlLog.list)
                .extracting(ILoggingEvent::getMessage)
                .allSatisfy(message -> assertThat(message).doesNotContainIgnoringCase("update"));
    }

    @DisplayName(" возвращать менеджер сессий")
    @Test
    void getSessionManager() {
        assertThat(clientDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }
}
