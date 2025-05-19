package com.itmo.mrdvd.collection.ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.CollectionMetadata;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.Ticket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class TicketCollection extends Collection<AuthoredTicket, Set<AuthoredTicket>> {
  private final CrudWorker<AuthoredTicket, Set<? extends AuthoredTicket>, Long> dbworker;
  private Set<AuthoredTicket> tickets;
  private TicketCollectionMetadata meta;

  public TicketCollection(
      CrudWorker<AuthoredTicket, Set<? extends AuthoredTicket>, Long> dbworker) {
    this(dbworker, "A new ticket collection");
  }

  public TicketCollection(
      CrudWorker<AuthoredTicket, Set<? extends AuthoredTicket>, Long> dbworker, String name) {
    this(dbworker, name, new HashSet<>());
  }

  public TicketCollection(
      CrudWorker<AuthoredTicket, Set<? extends AuthoredTicket>, Long> dbworker,
      String name,
      Set<AuthoredTicket> tickets) {
    this.dbworker = dbworker;
    this.tickets = tickets;
    this.meta = new TicketCollectionMetadata(name);
  }

  @Override
  public Optional<AuthoredTicket> add(AuthoredTicket obj, Predicate<AuthoredTicket> cond) {
    Optional<AuthoredTicket> ticket = dbworker.add(obj, cond);
    if (ticket.isPresent()) {
      tickets.add(ticket.get());
    }
    return ticket;
  }

  @Override
  public Optional<AuthoredTicket> get(Long id) {
    return this.tickets.stream().filter(ticket -> ticket.getId().equals(id)).findAny();
  }

  @Override
  public Iterator<AuthoredTicket> iterator() {
    if (this.tickets != null) {
      return tickets.iterator();
    }
    return Collections.emptyIterator();
  }

  @Override
  public Optional<AuthoredTicket> update(
      Long id, AuthoredTicket obj, Predicate<AuthoredTicket> cond) {
    Optional<AuthoredTicket> ticket = dbworker.update(id, obj, cond);
    if (ticket.isPresent()) {
      tickets.removeIf(t -> t.getId().equals(id));
      tickets.add(ticket.get());
    }
    return ticket;
  }

  @Override
  public void remove(Long id) {
    this.dbworker.remove(id);
    this.tickets.removeIf(ticket -> ticket.getId().equals(id));
  }

  @Override
  public Set<AuthoredTicket> getAll() {
    return this.tickets;
  }

  @Override
  public void setCache(Set<AuthoredTicket> cache) {
    this.tickets = cache;
  }

  @Override
  public void clearCache() {
    this.tickets.clear();
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
}
