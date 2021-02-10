package ru.otus.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class HibernateSessionFactoryBuilder {

    private static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";

    private String hibernateConfigFile = HIBERNATE_CONFIG_FILE;

    private final Map<String, String> additionalSettings = new HashMap<>();

    public SessionFactory build() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure(hibernateConfigFile)
                .applySettings(additionalSettings)
                .build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }

    public HibernateSessionFactoryBuilder setHibernateConfigFile(String configFile) {
        this.hibernateConfigFile = configFile;
        return this;
    }

    public HibernateSessionFactoryBuilder setDatabaseConfiguration(DatabaseConfiguration config) {
        setConnectionUrl(config.getUrl());
        setDbUsername(config.getUsername());
        setDbPassword(config.getPassword());
        return this;
    }

    public HibernateSessionFactoryBuilder setConnectionUrl(String dbUrl) {
        additionalSettings.put("hibernate.hikari.dataSource.url", dbUrl);
        return this;
    }

    public HibernateSessionFactoryBuilder setDbUsername(String dbUserName) {
        additionalSettings.put("hibernate.hikari.dataSource.user", dbUserName);
        return this;
    }

    public HibernateSessionFactoryBuilder setDbPassword(String dbPassword) {
        additionalSettings.put("hibernate.hikari.dataSource.password", dbPassword);
        return this;
    }
}
