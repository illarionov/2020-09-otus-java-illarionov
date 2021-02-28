package ru.x0xdc.otus.java.messagesystem.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;

@Configuration
public class MessageSystemConfig {
    public static final String DATABASE_MESSAGE_SERVICE_CLIENT_NAME = "database";
    public static final String FRONTEND_MESSAGE_SERVICE_CLIENT_NAME = "frontend";

    public static final String DATABASE_HANDLER_STORE_QUALIFIER = "databaseHandlerStore";
    public static final String DATABASE_MESSAGE_SERVICE_CLIENT_QUALIFIER = "databaseMsClient";

    public static final String FRONTEND_HANDLER_STORE_QUALIFIER = "frontendHandlerStore";
    public static final String FRONTEND_MESSAGE_SERVICE_CLIENT_QUALIFIER = "frontendMsClient";

    @Bean(initMethod = "start", destroyMethod = "dispose")
    MessageSystem messageSystem() {
        return new MessageSystemImpl(false);
    }

    @Bean
    CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean(name = DATABASE_MESSAGE_SERVICE_CLIENT_QUALIFIER)
    MsClient databaseMsClient(MessageSystem messageSystem,
                              @Qualifier(DATABASE_HANDLER_STORE_QUALIFIER) HandlersStore handlersStore,
                              CallbackRegistry callbackRegistry) {
        MsClient msClient = new MsClientImpl(DATABASE_MESSAGE_SERVICE_CLIENT_NAME, messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(msClient);
        return msClient;
    }

    @Bean(name = FRONTEND_MESSAGE_SERVICE_CLIENT_QUALIFIER)
    MsClient frontendMsClient(MessageSystem messageSystem,
                              @Qualifier(FRONTEND_HANDLER_STORE_QUALIFIER) HandlersStore handlersStore,
                              CallbackRegistry callbackRegistry) {
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_MESSAGE_SERVICE_CLIENT_NAME, messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }
}
