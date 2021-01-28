package ru.otus.web.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.WebServer;
import ru.otus.core.model.Role;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.web.auth.AuthenticationResult;
import ru.otus.web.auth.UserAuthService;
import ru.otus.web.template.TemplateProcessor;
import ru.otus.web.template.TemplateProcessorImpl;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static ru.otus.web.server.utils.WebServerHelper.*;

@DisplayName("Тест сервера должен ")
class UsersWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String LOGIN_URL = "login";
    private static final String ADMIN_USER_LIST_URL = "admin";

    private static final long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_LOGIN = "user1";
    private static final String DEFAULT_USER_PASSWORD = "11111";
    private static final User DEFAULT_USER = User.builder()
            .setId(DEFAULT_USER_ID)
            .setLogin(DEFAULT_USER_LOGIN)
            .setPassword(DEFAULT_USER_PASSWORD)
            .setRole(Role.ADMIN)
            .setName("Vasya")
            .build();
    private static final String INCORRECT_USER_LOGIN = "BadUser";

    private static UsersWebServer webServer;
    private static HttpClient http;

    @BeforeAll
    static void setUp() throws Exception {
        http = HttpClient.newHttpClient();

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(WebServer.TEMPLATES_DIR);
        DBServiceUser userService = mock(DBServiceUser.class);
        UserAuthService userAuthService = mock(UserAuthService.class);

        given(userAuthService.authenticate(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(AuthenticationResult.success(DEFAULT_USER_ID, Role.ADMIN));
        given(userAuthService.authenticate(INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(AuthenticationResult.failed());
        given(userService.getUser(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_USER));
        given(userService.findAllUsers()).willReturn(singletonList(DEFAULT_USER));

        webServer = new UsersWebServerImpl(WEB_SERVER_PORT, userAuthService, userService, templateProcessor);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать 302 при запросе пользователя по id если не выполнен вход ")
    @Test
    void shouldReturnForbiddenStatusForUserRequestWhenUnauthorized() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, ADMIN_USER_LIST_URL)))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
    }

    @DisplayName("возвращать ID сессии при выполнении входа с верными данными")
    @Test
    void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();
    }

    @DisplayName("не возвращать ID сессии при выполнении входа если данные входа не верны")
    @Test
    void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNull();
    }

    @DisplayName("возвращать корректные данные при запросе всех пользователей")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, ADMIN_USER_LIST_URL)))
                .setHeader(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body())
                .contains(DEFAULT_USER.getName());
    }
}