package ru.x0xdc.otus.java.messagesystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.x0xdc.otus.java.messagesystem.base.CustomPostgreSQLContainer;
import ru.x0xdc.otus.java.messagesystem.controllers.admin.AdminUserController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class MessageSystemApplicationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private AdminUserController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

}