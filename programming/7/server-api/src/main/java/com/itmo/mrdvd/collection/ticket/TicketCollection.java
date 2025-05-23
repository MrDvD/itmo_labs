package com.itmo.mrdvd.collection.ticket;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Predicate;

public class TicketCollection
    implements CachedCrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> {
  private final CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> dbworker;
  private Set<AuthoredTicket> tickets;
  private final ReadWriteLock objectCollectionLock;

  public TicketCollection(
      CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> dbworker,
      ReadWriteLock objectCollectionLock) {
    this(dbworker, objectCollectionLock, "A new ticket collection");
  }

  public TicketCollection(
      CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> dbworker,
      ReadWriteLock objectCollectionLock,
      String name) {
    this(dbworker, objectCollectionLock, name, new HashSet<>());
  }

  public TicketCollection(
      CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> dbworker,
      ReadWriteLock objectCollectionLock,
      String name,
      Set<AuthoredTicket> tickets) {
    this.dbworker = dbworker;
    this.tickets = tickets;
    this.objectCollectionLock = objectCollectionLock;
    setCache(this.dbworker.getAll());
  }

  @Override
  public Optional<AuthoredTicket> add(AuthoredTicket obj, Predicate<AuthoredTicket> cond) {
    this.objectCollectionLock.writeLock().lock();
    try {
      Optional<AuthoredTicket> ticket = dbworker.add(obj, cond);
      if (ticket.isPresent()) {
        tickets.add(ticket.get());
      }
      return ticket;
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Optional<AuthoredTicket> get(Long id) {
    this.objectCollectionLock.readLock().lock();
    try {
      return this.tickets.stream().filter(ticket -> ticket.getId().equals(id)).findAny();
    } finally {
      this.objectCollectionLock.readLock().unlock();
    }
  }

  @Override
  public Iterator<AuthoredTicket> iterator() {
    this.objectCollectionLock.readLock().lock();
    try {
      if (this.tickets != null) {
        return tickets.iterator();
      }
      return Collections.emptyIterator();
    } finally {
      this.objectCollectionLock.readLock().unlock();
    }
  }

  @Override
  public Optional<AuthoredTicket> update(
      Long id, AuthoredTicket obj, Predicate<AuthoredTicket> cond) {
    this.objectCollectionLock.writeLock().lock();
    try {
      Optional<AuthoredTicket> ticket = dbworker.update(id, obj, cond);
      if (ticket.isPresent()) {
        Optional<AuthoredTicket> toRemove = get(id);
        if (toRemove.isPresent()) {
          tickets.remove(toRemove.get());
          ticket.get().getEvent().setId(toRemove.get().getEvent().getId());
        }
        tickets.add(ticket.get());
      }
      return ticket;
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public void remove(Long id) {
    remove(id, (t) -> true);
  }

  @Override
  public void remove(Long id, Predicate<AuthoredTicket> cond) {
    this.objectCollectionLock.writeLock().lock();
    try {
      Optional<AuthoredTicket> toRemove = dbworker.get(id);
      if (toRemove.isPresent() && cond.test(toRemove.get())) {
        this.dbworker.remove(id);
        this.tickets.removeIf(ticket -> ticket.getId().equals(id));
      }
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Set<AuthoredTicket> getAll() {
    this.objectCollectionLock.readLock().lock();
    try {
      return this.tickets;
    } finally {
      this.objectCollectionLock.readLock().unlock();
    }
  }

  @Override
  public void setCache(Set<AuthoredTicket> cache) {
    this.objectCollectionLock.writeLock().lock();
    try {
      this.tickets = cache;
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public void clearCache() {
    this.objectCollectionLock.writeLock().lock();
    try {
      this.tickets.clear();
    } finally {
      this.objectCollectionLock.writeLock().lock();
    }
  }

  public int getCount() {
    this.objectCollectionLock.readLock().lock();
    try {
      return tickets.size();
    } finally {
      this.objectCollectionLock.readLock().unlock();
    }
  }

  @Override
  public void clear() {
    this.objectCollectionLock.writeLock().lock();
    try {
      this.dbworker.clear();
      this.tickets.clear();
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }
}
