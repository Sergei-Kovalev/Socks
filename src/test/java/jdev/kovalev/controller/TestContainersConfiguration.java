package jdev.kovalev.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")) {
            postgres.withDatabaseName("socks_db")
                    .withUsername("postgres")
                    .withPassword("postgres");
            postgres.start();
            return postgres;
        }
    }
}

