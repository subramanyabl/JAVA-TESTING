package com.example.retail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

  private final DBConnectionProvider connectionProvider;

  public OrderService(DBConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
    createOrdersTableIfNotExists();
  }

  public void createOrder(Order order) {
    try (Connection conn = this.connectionProvider.getConnection()) {
      PreparedStatement pstmt = conn.prepareStatement(
        "insert into orders(id,description) values(?,?)"
      );
      pstmt.setLong(1, order.id());
      pstmt.setString(2, order.description());
      pstmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();

    try (Connection conn = this.connectionProvider.getConnection()) {
      PreparedStatement pstmt = conn.prepareStatement(
        "select id,description from orders"
      );
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        long id = rs.getLong("id");
        String description = rs.getString("description");
        orders.add(new Order(id, description));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return orders;
  }

  public void deleteAllOrders() {
    try (Connection conn = this.connectionProvider.getConnection()) {
      PreparedStatement pstmt = conn.prepareStatement("delete from orders");
      pstmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void createOrdersTableIfNotExists() {
    try (Connection conn = this.connectionProvider.getConnection()) {
      PreparedStatement pstmt = conn.prepareStatement(
        """
        create table if not exists orders (
            id bigint not null,
            description varchar not null,
            primary key (id)
        )
        """
      );
      pstmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
