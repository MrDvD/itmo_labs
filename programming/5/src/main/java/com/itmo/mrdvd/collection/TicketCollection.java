package com.itmo.mrdvd.collection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.mrdvd.object.Ticket;

public class TicketCollection implements CollectionWorker<Ticket, List<Ticket>>, Iterable<Ticket> {
  @JsonProperty private List<Ticket> tickets;
  private final IdGenerator ticketGenerator;
  private final IdGenerator eventGenerator;
  private TicketCollectionMetadata meta;

  public static class TicketCollectionMetadata {
    @JsonProperty private LocalDateTime creationTime;
    @JsonProperty private String type;
    private String name;

    public TicketCollectionMetadata() {}

    public TicketCollectionMetadata(String name) {
      this.creationTime = LocalDateTime.now();
      this.type = Ticket.class.getSimpleName();
      this.name = name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public LocalDateTime getCreationTime() {
      return this.creationTime;
    }

    public String getName() {
      return this.name;
    }

    public String getType() {
      return this.type;
    }

    @Override
    public String toString() {
      String result = "";
      result += "# # # Метаданные коллекции # # #\n";
      result += String.format("НАЗВАНИЕ: %s\n", getName());
      result += String.format("ТИП: %s\n", getType());
      result +=
          String.format(
              "ДАТА ИНИЦИАЛИЗАЦИИ: %s\n",
              getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      // result += String.format("КОЛИЧЕСТВО ЭЛЕМЕНТОВ: %d", getCount());
      return result;
    }
  }

  public TicketCollection() {
    this(null, null, null);
  }

  public TicketCollection(String name, IdGenerator ticketGen, IdGenerator eventGen) {
    this.tickets = new ArrayList<>();
    this.meta = new TicketCollectionMetadata(name);
    this.ticketGenerator = ticketGen;
    this.eventGenerator = eventGen;
  }

  public Optional<Ticket> addRaw(Ticket obj) {
    if (obj.isValid()) {
      if (getTicketIdGenerator().isTaken(obj.getId())
          || getEventIdGenerator().isTaken(obj.getEvent().getId())) {
        return Optional.empty();
      }
      getTicketIdGenerator().takeId(obj.getId());
      getEventIdGenerator().takeId(obj.getEvent().getId());
      tickets.add(obj);
      return Optional.of(obj);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Ticket> add(Ticket obj) {
    if (obj == null || obj.getEvent() == null) {
      return Optional.empty();
    }
    Long ticketId = null, eventId = null;
    if (obj.getId() == null) {
      ticketId = getTicketIdGenerator().bookId(obj);
      obj.setId(ticketId);
    }
    if (obj.getEvent().getId() == null) {
      eventId = getEventIdGenerator().bookId(obj.getEvent());
      obj.getEvent().setId(eventId);
    }
    Optional<Ticket> ticket = addRaw(obj);
    if (ticket.isEmpty()) {
      getTicketIdGenerator().freeId(ticketId);
      getEventIdGenerator().freeId(eventId);
    }
    return ticket;
  }

  @Override
  public Optional<Ticket> get(Long id) {
    for (Ticket ticket : tickets) {
      if (ticket.getId().equals(id)) {
        return Optional.of(ticket);
      }
    }
    return Optional.empty();
  }

  @Override
  public Iterator<Ticket> iterator() {
    return tickets.iterator();
  }

  @Override
  public Optional<Ticket> update(Long id, Ticket obj) {
    if (obj.isValid()) {
      for (int i = 0; i < tickets.size(); i++) {
        Ticket ticket = tickets.get(i);
        if (ticket.getId().equals(id)) {
          tickets.set(i, obj);
          return Optional.of(obj);
        }
      }
   }
    return Optional.empty();
  }

  @Override
  public void remove(Long id) {
    for (int i = 0; i < tickets.size(); i++) {
      Ticket ticket = tickets.get(i);
      if (ticket.getId().equals(id)) {
        tickets.remove(i);
      }
    }
  }

  @Override
  public List<Ticket> getCollection() {
   return tickets;
  }

  public IdGenerator getTicketIdGenerator() {
    return ticketGenerator;
  }

  public IdGenerator getEventIdGenerator() {
    return eventGenerator;
  }

  public TicketCollectionMetadata getMetadata() {
    return this.meta;
  }

  public void setMetadata(TicketCollectionMetadata meta) {
    this.meta = meta;
  }

   public int getCount() {
      return tickets.size();
   }

  @Override
  public void clear() {
    tickets.clear();
  }
}
