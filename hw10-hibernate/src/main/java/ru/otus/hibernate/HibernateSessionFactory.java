package ru.otus.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class HibernateSessionFactory {

    private static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";

    private final Map<String, String> additionalSettings = new HashMap<>();

    public SessionFactory build() {
        return build(HIBERNATE_CONFIG_FILE);
    }

    private SessionFactory build(String hibernateConfigFile) {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure(hibernateConfigFile)
                .applySettings(additionalSettings)
                .build();
        
        Metadata metadata = new MetadataSources(serviceRegistry)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }

    public HibernateSessionFactory setConnectionUrl(String dbUrl) {
        additionalSettings.put("hibernate.hikari.dataSource.url", dbUrl);
        return this;
    }

    public HibernateSessionFactory setDbUsername(String dbUserName) {
        additionalSettings.put("hibernate.hikari.dataSource.user", dbUserName);
        return this;
    }

    public HibernateSessionFactory setDbPassword(String dbPassword) {
        additionalSettings.put("hibernate.hikari.dataSource.password", dbPassword);
        return this;
    }
}
