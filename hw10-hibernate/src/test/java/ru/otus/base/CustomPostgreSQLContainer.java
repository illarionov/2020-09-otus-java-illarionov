package ru.otus.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPostgreSQLContainer extends org.testcontainers.containers.PostgreSQLContainer<CustomPostgreSQLContainer> {

    private static final String IMAGE_VERSION = "postgres:12";

    private static final Logger logger = LoggerFactory.getLogger(CustomPostgreSQLContainer.class);

    private static volatile CustomPostgreSQLContainer container;

    public static CustomPostgreSQLContainer getInstance()  {
        if (container == null) {
            synchronized (CustomPostgreSQLContainer.class) {
                if (container == null) {
                    container = new CustomPostgreSQLContainer(IMAGE_VERSION)
                            .withUrlParam("stringtype", "unspecified")
                            .withUrlParam("TC_TMPFS", "/testtmpfs:rw")
                    ;
                }
            }
        }
        return container;
    }

    private CustomPostgreSQLContainer(String imageVersion) {
        super(imageVersion);
    }

    @Override
    public void start() {
        super.start();
        logger.info("postgres in docker started: url={}", getJdbcUrl());
    }
}
