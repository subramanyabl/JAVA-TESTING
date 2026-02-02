package com.example.retail;

import com.example.constants.DockerImageConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class OrderServiceWithJUnit5ExtensionTest {

  Logger logger = LoggerFactory.getLogger(OrderServiceWithJUnit5ExtensionTest.class);

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
          DockerImageConstants.POSTGRES_IMAGE
  );

  OrderService orderService;


  @BeforeEach
  void setUp() {
    String jdbcUrl = postgres.getJdbcUrl();
    String username = postgres.getUsername();
    String password = postgres.getPassword();
    Integer mappedPort = postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT);
    Integer containerPort = PostgreSQLContainer.POSTGRESQL_PORT; // Get the container port
    String databaseName = postgres.getDatabaseName();
    String containerId = postgres.getContainerId();

    // Log the connection details
    logger.info(
            "Database connection details: \n" +
                    "  Container ID: {}\n" +
                    "  URL: {}\n" +
                    "  Username: {}\n" +
                    "  Password: {}\n" +
                    "  Container Port (PostgreSQL): {}\n" + // Log container port
                    "  Mapped Port (Host): {}\n" + // Log mapped port
                    "  Database Name: {}",
            containerId,
            jdbcUrl,
            username,
            password,
            containerPort,
            mappedPort,
            databaseName
    );

    DBConnectionProvider connectionProvider = new DBConnectionProvider(
            jdbcUrl,
            username,
            password
    );
    orderService = new OrderService(connectionProvider);
    orderService.deleteAllOrders();
  }

  @Test
  void shouldGetCustomers() {
    orderService.createOrder(new Order(1L, "Laptop with 16GB RAM"));
    orderService.createOrder(new Order(2L, "Wireless Mechanical Keyboard"));

    List<Order> orders = orderService.getAllOrders();
    assertEquals(2, orders.size());
  }
}
