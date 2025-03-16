package com.itmo.mrdvd.collection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.mrdvd.builder.Builder;
import com.itmo.mrdvd.object.Ticket;

public class TicketCollection extends Collection<Ticket, List<Ticket>> {
  @JsonProperty private List<Ticket> tickets;
  private IdGenerator ticketGenerator;
  private IdGenerator eventGenerator;
  private TicketCollectionMetadata meta;

  public static class TicketCollectionMetadata implements CollectionMetadata {
    @JsonProperty private LocalDateTime creationTime;
    @JsonProperty private String type;
    private String name;

    protected TicketCollectionMetadata() {}

    protected TicketCollectionMetadata(String name) {
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

   protected static class TicketIdGenerator implements IdGenerator {
      private final Set<Long> usedIds;

      public TicketIdGenerator(Set<Long> usedIds) {
         this.usedIds = usedIds;
      }

      @Override
      public boolean isTaken(Long id) {
         return usedIds.contains(id);
      }

      @Override
      public void takeId(Long id) throws IllegalArgumentException {
         if (id == null) {
            throw new IllegalArgumentException("Id не может быть null.");
         }
         usedIds.add(id);
      }

      @Override
      public void freeId(Long id) {
         if (id != null) {
            usedIds.remove(id);
         }
      }

      @Override
      public Optional<Long> getId(Object obj) {
         if (obj == null) {
            return Optional.empty();
          }
          Long newId = Math.abs(Long.valueOf(obj.hashCode()));
          while (isTaken(newId) || newId == 0) {
            newId = Math.abs(newId + Math.round(Math.random() * 100000000000L - 50000000000L));
          }
          takeId(newId);
          return Optional.of(newId);
      }
   }

   public TicketCollection() {
      this("Безымянная коллекция билетов");
   }

  public TicketCollection(String name) {
   this(name, new TicketIdGenerator(new HashSet<>()), new TicketIdGenerator(new HashSet<>()));
  }

  public TicketCollection(String name, IdGenerator ticketGen, IdGenerator eventGen) {
    this.tickets = new ArrayList<>();
    this.meta = new TicketCollectionMetadata(name);
    this.ticketGenerator = ticketGen;
    this.eventGenerator = eventGen;
  }

  @Override
  public Optional<Ticket> add(Builder<Ticket> obj) {
   if (obj == null) {
      return Optional.empty();
   }
   Optional<Ticket> ticket = obj.build();
   if (ticket.isEmpty()) {
      return Optional.empty();
   }
   Optional<Long> ticketId = getTicketIdGenerator().getId(ticket.get());
   if (ticketId.isEmpty()) {
      return Optional.empty();
   }
   Optional<Long> eventId = getEventIdGenerator().getId(ticket.get().getEvent());
   if (eventId.isEmpty()) {
      getTicketIdGenerator().freeId(ticketId.get());
      return Optional.empty();
   }
   ticket.get().setId(ticketId.get());
   ticket.get().getEvent().setId(eventId.get());
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

  @Override
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
