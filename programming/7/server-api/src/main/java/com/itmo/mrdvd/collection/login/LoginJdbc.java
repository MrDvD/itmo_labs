package com.itmo.mrdvd.collection.login;

import com.fasterxml.jackson.dataformat.xml.XmlAnnotationIntrospector.Pair;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.LoginPasswordPair;
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

public class LoginJdbc implements CrudWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> {
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
  public Optional<LoginPasswordPair> add(LoginPasswordPair t, Predicate<LoginPasswordPair> cond) {
    String sql = "insert into USERS (name, passwd_hash) values (?, ?)";
    LoginPasswordPair pair = new LoginPasswordPair();
    pair.setLogin(t.getLogin());
    pair.setPassword(this.hash.hash(t.getPassword()));
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, pair.getLogin());
        stmt.setString(2, pair.getPassword());
        if (!cond.test(t)) {
          return Optional.empty();
        }
        stmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(pair);
  }

  @Override
  public Optional<LoginPasswordPair> update(
      String key, LoginPasswordPair obj, Predicate<LoginPasswordPair> cond) {
    String sql = "update USERS set passwd_hash = ? where name = ?";
    LoginPasswordPair pair = new LoginPasswordPair();
    pair.setLogin(obj.getLogin());
    pair.setPassword(this.hash.hash(obj.getPassword()));
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, pair.getPassword());
        stmt.setString(2, key);
        if (!cond.test(obj)) {
          return Optional.empty();
        }
        stmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(pair);
  }

  @Override
  public Optional<LoginPasswordPair> get(String key) {
    String sql = "select name, passwd_hash from USERS where name = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          LoginPasswordPair pair = new LoginPasswordPair();
          pair.setLogin(rs.getString("name"));
          pair.setPassword(rs.getString("passwd_hash"));
          return Optional.of(pair);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public Set<LoginPasswordPair> getAll() {
    return getAll(new HashSet<>());
  }

  public Set<LoginPasswordPair> getAll(Set<LoginPasswordPair> pairs) {
    String sql = "select name, passwd_hash from USERS";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        LoginPasswordPair pair = new LoginPasswordPair();
        pair.setLogin(rs.getString("name"));
        pair.setPassword(rs.getString("passwd_hash"));
        pairs.add(pair);
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
