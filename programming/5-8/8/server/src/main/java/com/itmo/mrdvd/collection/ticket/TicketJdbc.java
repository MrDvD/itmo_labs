package com.itmo.mrdvd.collection.ticket;

import com.google.protobuf.Timestamp;
import com.itmo.mrdvd.Coordinates;
import com.itmo.mrdvd.Event;
import com.itmo.mrdvd.EventType;
import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.NodeValue;
import com.itmo.mrdvd.ObjectId;
import com.itmo.mrdvd.Ticket;
import com.itmo.mrdvd.TicketType;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.mappers.Mapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class TicketJdbc implements CrudWorker<Node, Set<Node>, Long> {
  private final Mapper<LocalDateTime, Timestamp> timeMapper;
  private final String url;
  private final String user;
  private final String password;

  public TicketJdbc(
      Mapper<LocalDateTime, Timestamp> timeMapper, String url, String user, String password) {
    this.timeMapper = timeMapper;
    this.url = url;
    this.user = user;
    this.password = password;
  }

  @Override
  public Optional<Node> add(Node node, Predicate<Node> cond) {
    Ticket t = node.getItem().getTicket();
    if (t == null) {
      throw new IllegalArgumentException("Нельзя добавить узел без билета.");
    }
    if (!cond.test(node)) {
      return Optional.empty();
    }
    String sqlEvent = "insert into EVENTS (name, description, type) values (?, ?, ?::event_type)";
    String sqlTicket =
        "insert into TICKETS (name, x, y, price, type, event, author) values (?, ?, ?, ?, ?::ticket_type, ?, ?)";
    String sqlTicketDate = "select creation_date from TICKETS where id = ?";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      conn.setAutoCommit(false);
      Long eventId = null;
      try (PreparedStatement stmtEvent =
          conn.prepareStatement(sqlEvent, Statement.RETURN_GENERATED_KEYS)) {
        stmtEvent.setString(1, t.getEvent().getName());
        stmtEvent.setString(2, t.getEvent().getDesc());
        stmtEvent.setString(3, t.getEvent().getType().toString());
        stmtEvent.executeUpdate();
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
              conn.prepareStatement(sqlTicket, Statement.RETURN_GENERATED_KEYS);
          PreparedStatement stmtTicketDate = conn.prepareStatement(sqlTicketDate)) {
        stmtTicket.setString(1, t.getName());
        stmtTicket.setFloat(2, t.getCoords().getX());
        stmtTicket.setFloat(3, t.getCoords().getY());
        stmtTicket.setInt(4, t.getPrice());
        stmtTicket.setString(5, t.getType().toString());
        stmtTicket.setLong(6, eventId);
        stmtTicket.setString(7, node.getAuthor());
        stmtTicket.executeUpdate();
        try (ResultSet generatedKeys = stmtTicket.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            Long ticketId = generatedKeys.getLong(1);
            stmtTicketDate.setLong(1, ticketId);
            try (ResultSet creationDates = stmtTicketDate.executeQuery()) {
              if (creationDates.next()) {
                Optional<Timestamp> time =
                    this.timeMapper.convert(creationDates.getTimestamp(1).toLocalDateTime());
                if (time.isPresent()) {
                  Node newNode =
                      node.toBuilder()
                          .setItem(
                              NodeValue.newBuilder()
                                  .setTicket(
                                      node.getItem().getTicket().toBuilder()
                                          .setId(ObjectId.newBuilder().setId(ticketId).build())
                                          .setCreateDate(time.get())
                                          .setEvent(t.getEvent().toBuilder().setId(eventId).build())
                                          .build())
                                  .build())
                          .build();
                  conn.commit();
                  return Optional.of(newNode);
                }
              }
            }
          }
        }
      }
      conn.rollback();
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось добавить билет в коллекцию.");
    }
  }

  @Override
  public Optional<Node> update(Long id, Node node, Predicate<Node> cond) {
    Ticket t = node.getItem().getTicket();
    if (t == null) {
      throw new IllegalArgumentException("Нельзя добавить узел без билета.");
    }
    if (!cond.test(node)) {
      return Optional.empty();
    }
    String sqlEvent =
        "update EVENTS set name = ?, description = ?, type = ?::event_type where id = ?";
    String sqlEventId = "select event from TICKETS where id = ? limit 1";
    String sqlTicket =
        "update TICKETS set name = ?, x = ?, y = ?, price = ?, type = ?::ticket_type where id = ?";
    String sqlTicketDate = "select creation_date from TICKETS where id = ?";
    var result = node.getItem().getTicket().toBuilder();
    result.setId(ObjectId.newBuilder().setId(id).build());
    Long eventId = null;
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
      try (PreparedStatement stmtEventId = conn.prepareStatement(sqlEventId)) {
        stmtEventId.setLong(1, id);
        if (!stmtEventId.execute()) {
          return Optional.empty();
        }
        try (ResultSet eventIds = stmtEventId.getResultSet()) {
          if (eventIds.next()) {
            eventId = eventIds.getLong("event");
          } else {
            return Optional.empty();
          }
        }
      }
      conn.setAutoCommit(false);
      try (PreparedStatement stmtEvent = conn.prepareStatement(sqlEvent)) {
        stmtEvent.setString(1, t.getEvent().getName());
        stmtEvent.setString(2, t.getEvent().getDesc());
        stmtEvent.setString(3, t.getEvent().getType().toString());
        stmtEvent.setLong(4, eventId);
        stmtEvent.executeUpdate();
      }
      try (PreparedStatement stmtTicket = conn.prepareStatement(sqlTicket)) {
        stmtTicket.setString(1, t.getName());
        stmtTicket.setFloat(2, t.getCoords().getX());
        stmtTicket.setFloat(3, t.getCoords().getY());
        stmtTicket.setInt(4, t.getPrice());
        stmtTicket.setString(5, t.getType().toString());
        stmtTicket.setLong(6, t.getId().getId());
        stmtTicket.executeUpdate();
      }
      try (PreparedStatement stmtTicketDate = conn.prepareStatement(sqlTicketDate)) {
        stmtTicketDate.setLong(1, id);
        try (ResultSet dates = stmtTicketDate.executeQuery()) {
          if (dates.next()) {
            Optional<Timestamp> date =
                this.timeMapper.convert(dates.getTimestamp(1).toLocalDateTime());
            if (date.isPresent()) {
              Event newEvent =
                  node.getItem().getTicket().getEvent().toBuilder().setId(eventId).build();
              Ticket newTicket =
                  node.getItem().getTicket().toBuilder()
                      .setId(ObjectId.newBuilder().setId(id).build())
                      .setCreateDate(date.get())
                      .setEvent(newEvent)
                      .build();
              Node newNode =
                  node.toBuilder()
                      .setItem(NodeValue.newBuilder().setTicket(newTicket).build())
                      .build();
              conn.commit();
              return Optional.of(newNode);
            }
          }
        }
      }
      conn.rollback();
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось обновить билет в коллекции.");
    }
  }

  @Override
  public Optional<Node> get(Long id) {
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
          Optional<Timestamp> time =
              this.timeMapper.convert(rs.getTimestamp("creation_date").toLocalDateTime());
          if (time.isPresent()) {
            return Optional.ofNullable(
                Node.newBuilder()
                    .setItem(
                        NodeValue.newBuilder()
                            .setTicket(
                                Ticket.newBuilder()
                                    .setId(ObjectId.newBuilder().setId(rs.getLong("id")).build())
                                    .setName(rs.getString("name"))
                                    .setCoords(
                                        Coordinates.newBuilder()
                                            .setX(rs.getFloat("x"))
                                            .setY(rs.getFloat("y"))
                                            .build())
                                    .setPrice(rs.getInt("price"))
                                    .setType(TicketType.valueOf(rs.getString("type")))
                                    .setEvent(
                                        Event.newBuilder()
                                            .setId(rs.getLong("event_id"))
                                            .setName(rs.getString("event_name"))
                                            .setDesc(rs.getString("event_description"))
                                            .setType(EventType.valueOf(rs.getString("event_type")))
                                            .build())
                                    .setCreateDate(time.get())
                                    .build())
                            .build())
                    .setAuthor(rs.getString("author"))
                    .build());
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось получить билет из коллекции.");
    }
    return Optional.empty();
  }

  @Override
  public Set<Node> getAll() {
    return getAll(new HashSet<>());
  }

  public Set<Node> getAll(Set<Node> tickets) {
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
        var event = Event.newBuilder();
        event.setId(rs.getLong("event_id"));
        event.setName(rs.getString("event_name"));
        event.setDesc(rs.getString("event_description"));
        event.setType(EventType.valueOf(rs.getString("event_type")));
        var coords = Coordinates.newBuilder();
        coords.setX(rs.getFloat("x"));
        coords.setY(rs.getFloat("y"));
        var ticket = Ticket.newBuilder();
        ticket.setId(ObjectId.newBuilder().setId(rs.getLong("id")).build());
        ticket.setName(rs.getString("name"));
        ticket.setCoords(coords.build());
        Instant instant =
            rs.getTimestamp("creation_date")
                .toLocalDateTime()
                .atZone(ZoneId.systemDefault())
                .toInstant();
        ticket.setCreateDate(
            Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build());
        ticket.setPrice(rs.getInt("price"));
        ticket.setType(TicketType.valueOf(rs.getString("type")));
        ticket.setEvent(event.build());
        tickets.add(
            Node.newBuilder()
                .setItem(NodeValue.newBuilder().setTicket(ticket.build()).build())
                .setAuthor(rs.getString("author"))
                .build());
      }
      return tickets;
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось получить билеты из коллекции.");
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
      throw new RuntimeException("Не удалось удалить билет из коллекции.");
    }
  }

  @Override
  public void clear() {
    String sql = "truncate TICKETS, EVENTS cascade";
    try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось очистить коллекцию.");
    }
  }
}
