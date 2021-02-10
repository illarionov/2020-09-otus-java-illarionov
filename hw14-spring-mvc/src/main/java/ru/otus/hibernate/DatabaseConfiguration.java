package ru.otus.hibernate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class DatabaseConfiguration {

    private static final String DATABASE_CONFIG_FILE = "database.cfg";

    public static final String PROP_DATABASE_URL = "url";
    public static final String PROP_DATABASE_USER = "user";
    public static final String PROP_DATABASE_PASSWORD = "password";

    private final Properties properties;

    public static DatabaseConfiguration create() {
        return create(DATABASE_CONFIG_FILE);
    }

    public static DatabaseConfiguration create(String filename) {
        Properties props = new Properties();

        try (var inputStream = DatabaseConfiguration.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new FileNotFoundException("property file '" + filename + "' not found in the classpath");
            }
            props.load(inputStream);
            for (String prop: List.of(PROP_DATABASE_URL, PROP_DATABASE_USER, PROP_DATABASE_PASSWORD)) {
                if (!props.containsKey(prop)) throw new IllegalStateException("Property `" + prop + "` not defined in configuration file `" + filename + "`");
            }
            return new DatabaseConfiguration(props);
        } catch (IOException e) {
            throw new RuntimeException("can not load property file " + filename);
        }
    }

    private DatabaseConfiguration(Properties properties) {
        this.properties = properties;
    }

    public String getUrl() {
        return properties.getProperty(PROP_DATABASE_URL);
    }

    public String getUsername() {
        return properties.getProperty(PROP_DATABASE_USER);
    }

    public String getPassword() {
        return properties.getProperty(PROP_DATABASE_PASSWORD);
    }
}
