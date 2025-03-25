package com.itmo.mrdvd.collection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.mrdvd.builder.builders.Builder;
import com.itmo.mrdvd.builder.updaters.Updater;
import com.itmo.mrdvd.builder.validators.Validator;
import com.itmo.mrdvd.object.Ticket;

public class TicketCollection extends Collection<Ticket, List<Ticket>> {
  @JsonProperty private List<Ticket> tickets;
  private IdGenerator ticketGenerator;
  private IdGenerator eventGenerator;
  @JsonProperty private TicketCollectionMetadata meta;

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

  protected Optional<Ticket> acquireId(Ticket obj) {
   Optional<Long> ticketId = getTicketIdGenerator().getId(obj);
   if (ticketId.isEmpty()) {
      return Optional.empty();
   }
   Optional<Long> eventId = getEventIdGenerator().getId(obj.getEvent());
   if (eventId.isEmpty()) {
      getTicketIdGenerator().freeId(ticketId.get());
      return Optional.empty();
   }
   obj.setId(ticketId.get());
   obj.getEvent().setId(eventId.get());
   return Optional.of(obj);
  }

  @Override
  public Optional<Ticket> add(Builder<Ticket> obj) throws IllegalArgumentException {
   return add(obj, null, null);
  }

  @Override
  public Optional<Ticket> add(Builder<Ticket> obj, Comparator<Ticket> cond, Set<Integer> values) throws IllegalArgumentException {
      if (obj == null) {
         throw new IllegalArgumentException("Builder не может быть null.");
      }
      Optional<Ticket> ticket = obj.build();
      if (ticket.isEmpty()) {
         return Optional.empty();
      }
      Optional<Ticket> result = acquireId(ticket.get());
      if (result.isEmpty()) {
         return Optional.empty();
      }
      Ticket ticketWithId = result.get();
      if (cond != null) {
         if (values == null) {
            throw new IllegalArgumentException("Набор значений для сравнения не может быть null.");
         }
         for (Ticket t : tickets) {
            if (!values.contains(cond.compare(ticketWithId, t))) {
               return Optional.empty();
            }
         }
      }
      tickets.add(ticketWithId);
      return result;
   }

   @Override
   public Optional<Ticket> add(Ticket rawObject, Validator<Ticket> validator) throws IllegalArgumentException {
      if (rawObject == null) {
         throw new IllegalArgumentException("Объект не может быть null.");
      }
      if (validator != null && !validator.validate(rawObject)) {
         return Optional.empty();
      }
      Optional<Ticket> result = acquireId(rawObject);
      if (result.isEmpty()) {
         return Optional.empty();
      }
      tickets.add(result.get());
      return result;
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
  public Optional<Ticket> update(Long id, Updater<Ticket> updater) {
   return update(id, updater, null, null);
  }

  @Override
  public Optional<Ticket> update(Long id, Updater<Ticket> updater, Comparator<Ticket> cond, Set<Integer> values) throws IllegalArgumentException {
   for (int i = 0; i < tickets.size(); i++) {
      Ticket ticket = tickets.get(i);
      if (ticket.getId().equals(id)) {
         Optional<Ticket> obj = updater.update(ticket);
         if (obj.isPresent()) {
            if (cond != null && values == null) {
               throw new IllegalArgumentException("Набор значений для сравнения не может быть null.");
            }
            if (cond == null || values.contains(cond.compare(obj.get(), ticket))) {
               tickets.set(i, obj.get());
               return obj;
            }
            return Optional.empty();
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

  @JsonIgnore
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

  // looks bad, but idk how to code this better
  @JsonIgnore
  @Override
  public void setMetadata(CollectionMetadata meta) {
    // ofc i can add 'instanceof' check, but it would be bad anyways
    this.meta = (TicketCollectionMetadata) meta;
  }

   public int getCount() {
      return tickets.size();
   }

  @Override
  public void clear() {
    tickets.clear();
  }
}
