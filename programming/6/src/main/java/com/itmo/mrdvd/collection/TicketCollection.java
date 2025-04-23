package com.itmo.mrdvd.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.mrdvd.builder.validators.Validator;
import com.itmo.mrdvd.object.Ticket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

  protected boolean isValidTicketIds(Ticket obj) {
    return obj.getId() != null
        && !this.getTicketIdGenerator().isTaken(obj.getId())
        && obj.getEvent().getId() != null
        && !this.getEventIdGenerator().isTaken(obj.getEvent().getId());
  }

  protected Optional<Ticket> acquireId(Ticket obj) {
    if (isValidTicketIds(obj)) {
      getTicketIdGenerator().takeId(obj.getId());
      getEventIdGenerator().takeId(obj.getEvent().getId());
      return Optional.of(obj);
    }
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
  public Optional<Ticket> add(Ticket obj, Validator<Ticket> validator)
      throws IllegalArgumentException {
    return add(obj, validator, null, null);
  }

  @Override
  public Optional<Ticket> add(
      Ticket obj, Validator<Ticket> validator, Comparator<Ticket> cond, Set<Integer> values)
      throws IllegalArgumentException {
    if (validator == null) {
      throw new IllegalArgumentException("Не задан валидатор для объекта.");
    }
    if (!validator.validate(obj)) {
      return Optional.empty();
    }
    Optional<Ticket> result = acquireId(obj);
    if (result.isEmpty()) {
      return Optional.empty();
    }
    Ticket ticketWithId = result.get();
    if (cond != null) {
      if (values == null) {
        throw new IllegalArgumentException("Не задан набор значений для сравнения.");
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
  public Optional<Ticket> update(Long id, Ticket obj, Validator<Ticket> validator) {
    return update(id, obj, validator, null, null);
  }

  @Override
  public Optional<Ticket> update(
      Long id,
      Ticket obj,
      Validator<Ticket> validator,
      Comparator<Ticket> cond,
      Set<Integer> values)
      throws IllegalArgumentException {
    if (validator == null) {
      throw new IllegalArgumentException("Не задан валидатор для объекта.");
    }
    if (!validator.validate(obj) || !isValidTicketIds(obj)) {
      return Optional.empty();
    }
    for (int i = 0; i < tickets.size(); i++) {
      Ticket ticket = tickets.get(i);
      if (ticket.getId().equals(id)) {
        if (cond != null && values == null) {
          throw new IllegalArgumentException("Не задан набор значений для сравнения.");
        }
        if (cond == null || values.contains(cond.compare(obj, ticket))) {
          tickets.set(i, obj);
          return Optional.of(obj);
        }
        return Optional.empty();
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
