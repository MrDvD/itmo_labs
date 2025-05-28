package com.itmo.mrdvd.collection.ticket;

import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.CrudWorker;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Predicate;

public class TicketCollection implements CachedCrudWorker<Node, Set<Node>, Long> {
  private final CrudWorker<Node, Set<Node>, Long> dbworker;
  private Set<Node> tickets;
  private final ReadWriteLock objectCollectionLock;

  public TicketCollection(
      CrudWorker<Node, Set<Node>, Long> dbworker, ReadWriteLock objectCollectionLock) {
    this(dbworker, objectCollectionLock, "A new ticket collection");
  }

  public TicketCollection(
      CrudWorker<Node, Set<Node>, Long> dbworker, ReadWriteLock objectCollectionLock, String name) {
    this(dbworker, objectCollectionLock, name, new HashSet<>());
  }

  public TicketCollection(
      CrudWorker<Node, Set<Node>, Long> dbworker,
      ReadWriteLock objectCollectionLock,
      String name,
      Set<Node> tickets) {
    this.dbworker = dbworker;
    this.tickets = tickets;
    this.objectCollectionLock = objectCollectionLock;
    setCache(this.dbworker.getAll());
  }

  @Override
  public Optional<Node> add(Node obj, Predicate<Node> cond) {
    this.objectCollectionLock.writeLock().lock();
    try {
      Optional<Node> ticket = dbworker.add(obj, cond);
      if (ticket.isPresent()) {
        tickets.add(ticket.get());
      }
      return ticket;
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Optional<Node> get(Long id) {
    this.objectCollectionLock.readLock().lock();
    try {
      return this.tickets.stream()
          .filter(ticket -> Long.valueOf(ticket.getItem().getTicket().getId()).equals(id))
          .findAny();
    } finally {
      this.objectCollectionLock.readLock().unlock();
    }
  }

  @Override
  public Iterator<Node> iterator() {
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
  public Optional<Node> update(Long id, Node obj, Predicate<Node> cond) {
    this.objectCollectionLock.writeLock().lock();
    try {
      Optional<Node> ticket = dbworker.update(id, obj, cond);
      if (ticket.isPresent()) {
        Optional<Node> toRemove = get(id);
        if (toRemove.isPresent()) {
          tickets.remove(toRemove.get());
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
  public void remove(Long id, Predicate<Node> cond) {
    this.objectCollectionLock.writeLock().lock();
    try {
      Optional<Node> toRemove = dbworker.get(id);
      if (toRemove.isPresent() && cond.test(toRemove.get())) {
        this.dbworker.remove(id);
        this.tickets.removeIf(
            ticket -> Long.valueOf(ticket.getItem().getTicket().getId()).equals(id));
      }
    } finally {
      this.objectCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Set<Node> getAll() {
    this.objectCollectionLock.readLock().lock();
    try {
      return this.tickets;
    } finally {
      this.objectCollectionLock.readLock().unlock();
    }
  }

  @Override
  public void setCache(Set<Node> cache) {
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
