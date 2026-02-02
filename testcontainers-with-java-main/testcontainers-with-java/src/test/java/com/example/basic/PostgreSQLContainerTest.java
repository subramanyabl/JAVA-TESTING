package com.example.basic;

import com.example.constants.DockerImageConstants;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PostgreSQLContainerTest {
    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLContainerTest.class);

    /**
     * This test succeeds without manually setting environment variables.
     * `PostgreSQLContainer` from Testcontainers automatically provides default credentials:
     *    - Username: "test"
     *    - Password: "test"
     *    - Database: "test"
     */
    @Test
    void shouldSucceedWithPostgreSQLContainer() {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageConstants.POSTGRES_IMAGE)) {
            logger.info("Starting PostgreSQL container using PostgreSQLContainer...");

            postgres.start(); // Should start successfully

            logger.info("PostgreSQL container started successfully. Status: {}", postgres.isRunning());
            assertTrue(postgres.isRunning(), "PostgreSQL container should be running.");

            // Logging default credentials provided by `PostgreSQLContainer`
            logger.info("JDBC URL: {}", postgres.getJdbcUrl());
            logger.info("Username: {}", postgres.getUsername());
            logger.info("Password: {}", postgres.getPassword());

        } catch (Exception e) {
            logger.error("Unexpected failure: {}", e.getMessage(), e);
            fail("Unexpected failure: " + e.getMessage());
        }
    }
}
