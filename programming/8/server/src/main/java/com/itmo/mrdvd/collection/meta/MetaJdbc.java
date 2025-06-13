package com.itmo.mrdvd.collection.meta;

import com.itmo.mrdvd.collection.AccessWorker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class MetaJdbc implements AccessWorker<Map<String, Object>> {
  private final String url;
  private final String user;
  private final String password;

  public MetaJdbc(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  @Override
  public Optional<Map<String, Object>> get() {
    String sql = "select * from COLLECTIONS limit 1";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement()) {
      try (ResultSet rs = stmt.executeQuery(sql)) {
        if (rs.next()) {
          return Optional.of(
              Map.of(
                  "name",
                  rs.getString("name"),
                  "type",
                  rs.getString("type"),
                  "creation_date",
                  rs.getTimestamp("creation_date").toLocalDateTime()));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось получить метаданные коллекции.");
    }
    return Optional.empty();
  }

  @Override
  public void set(Map<String, Object> meta) {
    if (!meta.containsKey("name")
        || !meta.containsKey("type")
        || !meta.containsKey("creation_date")) {
      throw new IllegalArgumentException("Отсутствуют требуемые метаданные для вставки.");
    }
    String sql1 = "truncate COLLECTIONS cascade";
    String sql2 = "insert into COLLECTIONS(name, type, creation_date) values (?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(sql1);
      }
      try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
        stmt.setString(1, (String) meta.get("name"));
        stmt.setString(2, (String) meta.get("type"));
        stmt.setTimestamp(3, Timestamp.valueOf((LocalDateTime) meta.get("creation_date")));
        stmt.executeUpdate();
      }
    } catch (SQLException | ClassCastException e) {
      throw new RuntimeException("Не удалось обновить метаданные коллекции.");
    }
  }
}
