package ru.otus.dbservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceException;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@DisplayName("Сервис для работы с клиентами в рамках БД должен ")
@ExtendWith(MockitoExtension.class)
class DbServiceUserImplTest {

    private static final long USER_ID = 1L;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private UserDao userDao;

    private DbServiceUserImpl dbServiceClient;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        given(userDao.getSessionManager()).willReturn(sessionManager);
        inOrder = inOrder(userDao, sessionManager);
        dbServiceClient = new DbServiceUserImpl(userDao);
    }

    @Test
    @DisplayName(" корректно сохранять клиента")
    void shouldCorrectSaveClient() {
        var vasya = User.builder().build();
        given(userDao.insertOrUpdate(vasya)).willReturn(USER_ID);
        long id = dbServiceClient.saveUser(vasya);
        assertThat(id).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName(" при сохранении клиента, открывать и коммитить транзакцию в нужном порядке")
    void shouldCorrectSaveClientAndOpenAndCommitTranInExpectedOrder() {
        dbServiceClient.saveUser(User.builder().build());

        inOrder.verify(userDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).commitSession();
        inOrder.verify(sessionManager, never()).rollbackSession();
    }

    @Test
    @DisplayName(" при сохранении клиента, открывать и откатывать транзакцию в нужном порядке")
    void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
        doThrow(IllegalArgumentException.class).when(userDao).insertOrUpdate(any());

        assertThatThrownBy(() -> dbServiceClient.saveUser(null))
                .isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

        inOrder.verify(userDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).rollbackSession();
        inOrder.verify(sessionManager, never()).commitSession();
    }

    @Test
    @DisplayName(" корректно загружать клиента по заданному id")
    void shouldLoadCorrectClientById() {
        User expectedClient = User.builder()
                .setId(USER_ID)
                .setName("Вася")
                .setAge(50)
                .build();
        given(userDao.findById(USER_ID)).willReturn(Optional.of(expectedClient));
        Optional<User> mayBeClient = dbServiceClient.getUser(USER_ID);
        assertThat(mayBeClient).isPresent().get().isEqualTo(expectedClient);
    }
}
