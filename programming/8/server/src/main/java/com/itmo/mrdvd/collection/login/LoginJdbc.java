package com.itmo.mrdvd.collection.login;

import com.itmo.mrdvd.Credentials;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.collection.SelfContainedHash;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class LoginJdbc implements CrudWorker<Credentials, Set<Credentials>, String> {
  private final String url;
  private final String user;
  private final String password;
  private final SelfContainedHash hash;

  public LoginJdbc(String url, String user, String password, SelfContainedHash hash) {
    this.url = url;
    this.user = user;
    this.password = password;
    this.hash = hash;
  }

  @Override
  public Optional<Credentials> add(Credentials t, Predicate<Credentials> cond) {
    String sql = "insert into USERS (name, passwd_hash) values (?, ?)";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, t.getLogin());
        stmt.setString(2, this.hash.hash(t.getPassword()));
        if (!cond.test(t)) {
          return Optional.empty();
        }
        stmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(t);
  }

  @Override
  public Optional<Credentials> update(String key, Credentials obj, Predicate<Credentials> cond) {
    String sql = "update USERS set passwd_hash = ? where name = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, this.hash.hash(obj.getPassword()));
        stmt.setString(2, key);
        if (!cond.test(obj)) {
          return Optional.empty();
        }
        stmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(obj);
  }

  @Override
  public Optional<Credentials> get(String key) {
    String sql = "select name, passwd_hash from USERS where name = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          return Optional.of(
              Credentials.newBuilder()
                  .setLogin(rs.getString("name"))
                  .setPassword(rs.getString("passwd_hash"))
                  .build());
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public Set<Credentials> getAll() {
    return getAll(new HashSet<>());
  }

  public Set<Credentials> getAll(Set<Credentials> pairs) {
    String sql = "select name, passwd_hash from USERS";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        pairs.add(
            Credentials.newBuilder()
                .setLogin(rs.getString("name"))
                .setPassword(rs.getString("passwd_hash"))
                .build());
      }
      return pairs;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(String key) {
    String sql = "delete from USERS where name = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, key);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() {
    String sql = "truncate USERS cascade";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
