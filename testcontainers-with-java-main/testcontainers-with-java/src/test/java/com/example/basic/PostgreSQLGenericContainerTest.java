package com.example.basic;

import com.example.constants.DockerImageConstants;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

public class PostgreSQLGenericContainerTest {
    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLGenericContainerTest.class);

    /**
     * This test succeeds as  we explicitly provide `POSTGRES_USER`, `POSTGRES_PASSWORD`, and `POSTGRES_DB`.
     * These environment variables allow PostgreSQL to start successfully.
     */
    @Test
    void shouldSucceedWithGenericContainerWithEnv() {
        GenericContainer<?> postgres = new GenericContainer<>(DockerImageConstants.POSTGRES_IMAGE)
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", "testuser")       // Setting user
                .withEnv("POSTGRES_PASSWORD", "testpassword") // Setting required password
                .withEnv("POSTGRES_DB", "testdb") ;

        logger.info("Starting PostgreSQL container with environment variables...");

        postgres.start(); // Should start successfully

        logger.info("PostgreSQL container started successfully. Status: {}", postgres.isRunning());
        assertTrue(postgres.isRunning(), "PostgreSQL container should be running.");

        postgres.stop();
    }

    /**
     * This test demonstrates failure when using `GenericContainer` without setting environment variables*
     * PostgreSQL requires `POSTGRES_PASSWORD`, but it is missing here, causing container startup failure.
     */
    @Test
    void shouldFailWithGenericContainerWithoutEnv() {
        GenericContainer<?> postgres = new GenericContainer<>(DockerImageConstants.POSTGRES_IMAGE)
                .withExposedPorts(5432); // No environment variables provided

        logger.info("Attempting to start PostgreSQL container without environment variables...");

        Exception exception = assertThrows(Exception.class, postgres::start);
        logger.error("Expected failure due to missing POSTGRES_PASSWORD: {}", exception.getMessage());

        assertFalse(postgres.isRunning(), "PostgreSQL container should NOT be running.");
    }

    /**
     * This test **succeeds** without manually setting environment variables.
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
