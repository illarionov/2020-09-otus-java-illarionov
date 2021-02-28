package ru.x0xdc.otus.java.messagesystem.controllers.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.x0xdc.otus.java.messagesystem.base.CustomPostgreSQLContainer;
import ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig;
import ru.x0xdc.otus.java.messagesystem.core.model.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig.WEB_SOCKET_ENDPOINT;
import static ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig.WS_DEST_USER_LIST_BROADCAST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AdminUserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserControllerTest.class);

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

    @LocalServerPort
    private Integer port;

    @Autowired
    private AdminUserController controller;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private WebSocketStompClient webSocketStompClient;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    void broadcastUserListShouldWork() throws Exception {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d%s", port, WEB_SOCKET_ENDPOINT),
                        new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        
        session.subscribe(WS_DEST_USER_LIST_BROADCAST, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }
            
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                String json = new String((byte[]) payload);
                logger.info("Received message: " + payload);
                blockingQueue.add(json);
                session.disconnect();
            }
        });

        session.send("/app" + WebSocketConfig.WS_DEST_USER_LIST_REQUEST, "");

        String jsonString = blockingQueue.poll(1, TimeUnit.SECONDS);
        List<User> users = jacksonObjectMapper.readValue(jsonString, new TypeReference<List<User>>() {});

        assertThat(users)
                .extracting(User::getId, User::getLogin)
                .contains(Tuple.tuple(1L, "admin"));
    }
}