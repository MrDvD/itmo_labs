package com.itmo.mrdvd.collection.ticket;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;
import com.itmo.mrdvd.object.TicketType;
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

public class TicketJdbc implements CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> {
  private final String url;
  private final String user;
  private final String password;

  public TicketJdbc(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  @Override
  public Optional<AuthoredTicket> add(AuthoredTicket t, Predicate<AuthoredTicket> cond) {
    String sqlEvent = "insert into EVENTS (name, description, type) values (?, ?, ?::event_type)";
    String sqlTicket =
        "insert into TICKETS (name, x, y, price, type, event, author) values (?, ?, ?, ?, ?::ticket_type, ?, ?)";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      conn.setAutoCommit(false);
      Long eventId = null;
      try (PreparedStatement stmtEvent =
          conn.prepareStatement(sqlEvent, Statement.RETURN_GENERATED_KEYS)) {
        stmtEvent.setString(1, t.getEvent().getName());
        stmtEvent.setString(2, t.getEvent().getDescription());
        stmtEvent.setString(3, t.getEvent().getType().toString());
        if (stmtEvent.executeUpdate() == 0) {
          conn.rollback();
          return Optional.empty();
        }
        try (ResultSet generatedKeys = stmtEvent.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            eventId = generatedKeys.getLong(1);
          } else {
            conn.rollback();
            return Optional.empty();
          }
        }
      }

      try (PreparedStatement stmtTicket =
          conn.prepareStatement(sqlTicket, Statement.RETURN_GENERATED_KEYS)) {
        stmtTicket.setString(1, t.getName());
        stmtTicket.setFloat(2, t.getCoordinates().getX());
        stmtTicket.setFloat(3, t.getCoordinates().getY());
        stmtTicket.setInt(4, t.getPrice());
        stmtTicket.setString(5, t.getType().toString());
        stmtTicket.setLong(6, eventId);
        stmtTicket.setString(7, t.getAuthor());
        if (stmtTicket.executeUpdate() == 0) {
          conn.rollback();
          return Optional.empty();
        }
        try (ResultSet generatedKeys = stmtTicket.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            t.setId(generatedKeys.getLong(1));
            t.getEvent().setId(eventId);
            if (cond.test(t)) {
              conn.commit();
              return Optional.of(t);
            }
          }
          conn.rollback();
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthoredTicket> update(
      Long id, AuthoredTicket t, Predicate<AuthoredTicket> cond) {
    String sqlEvent =
        "update EVENTS set name = ?, description = ?, type = ?::event_type where id = (select event from TICKETS where id = ?)";
    String sqlTicket = "update TICKETS set name = ?, x = ?, y = ?, price = ?, type = ?::ticket_type where id = ?";
    t.setId(id);
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      conn.setAutoCommit(false);
      try (PreparedStatement stmtEvent = conn.prepareStatement(sqlEvent)) {
        stmtEvent.setString(1, t.getEvent().getName());
        stmtEvent.setString(2, t.getEvent().getDescription());
        stmtEvent.setString(3, t.getEvent().getType().toString());
        stmtEvent.setLong(4, t.getId());
        if (stmtEvent.executeUpdate() == 0) {
          conn.rollback();
          return Optional.empty();
        }
      }
      try (PreparedStatement stmtTicket = conn.prepareStatement(sqlTicket)) {
        stmtTicket.setString(1, t.getName());
        stmtTicket.setFloat(2, t.getCoordinates().getX());
        stmtTicket.setFloat(3, t.getCoordinates().getY());
        stmtTicket.setInt(4, t.getPrice());
        stmtTicket.setString(5, t.getType().toString());
        stmtTicket.setLong(6, t.getId());
        if (stmtTicket.executeUpdate() == 0) {
          conn.rollback();
          return Optional.empty();
        }
      }
      if (cond.test(t)) {
        conn.commit();
        return Optional.of(t);
      }
      conn.rollback();
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthoredTicket> get(Long id) {
    String sql =
        "select t.id, t.name, t.x, t.y, t.creation_date, t.price, t.type, "
            + "t.author, e.id as event_id, e.name as event_name, "
            + "e.description as event_description, e.type as event_type "
            + "from TICKETS t "
            + "join EVENTS e on t.event = e.id "
            + "where t.id = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          Event event = new Event();
          event.setId(rs.getLong("event_id"));
          event.setName(rs.getString("event_name"));
          event.setDescription(rs.getString("event_description"));
          event.setType(EventType.valueOf(rs.getString("event_type")));
          Coordinates coords = new Coordinates();
          coords.setX(rs.getFloat("x"));
          coords.setY(rs.getFloat("y"));
          AuthoredTicket ticket = new AuthoredTicket();
          ticket.setId(rs.getLong("id"));
          ticket.setName(rs.getString("name"));
          ticket.setCoordinates(coords);
          ticket.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
          ticket.setPrice(rs.getInt("price"));
          ticket.setType(TicketType.valueOf(rs.getString("type")));
          ticket.setEvent(event);
          ticket.setAuthor(rs.getString("author"));
          return Optional.of(ticket);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public Set<AuthoredTicket> getAll() {
    return getAll(new HashSet<>());
  }

  public Set<AuthoredTicket> getAll(Set<AuthoredTicket> tickets) {
    String sql =
        "select t.id, t.name, t.x, t.y, t.creation_date, t.price, t.type, "
            + "t.author, e.id as event_id, e.name as event_name, "
            + "e.description as event_description, e.type as event_type "
            + "from TICKETS t "
            + "join EVENTS e on t.event = e.id ";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Event event = new Event();
        event.setId(rs.getLong("event_id"));
        event.setName(rs.getString("event_name"));
        event.setDescription(rs.getString("event_description"));
        event.setType(EventType.valueOf(rs.getString("event_type")));
        Coordinates coords = new Coordinates();
        coords.setX(rs.getFloat("x"));
        coords.setY(rs.getFloat("y"));
        AuthoredTicket ticket = new AuthoredTicket();
        ticket.setId(rs.getLong("id"));
        ticket.setName(rs.getString("name"));
        ticket.setCoordinates(coords);
        ticket.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        ticket.setPrice(rs.getInt("price"));
        ticket.setType(TicketType.valueOf(rs.getString("type")));
        ticket.setEvent(event);
        ticket.setAuthor(rs.getString("author"));
        tickets.add(ticket);
      }
      return tickets;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(Long id) {
    String sql = "delete from TICKETS where id = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() {
    String sql = "truncate TICKETS, EVENTS cascade";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
