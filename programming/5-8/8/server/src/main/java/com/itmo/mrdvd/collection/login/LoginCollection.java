package com.itmo.mrdvd.collection.login;

import com.itmo.mrdvd.Credentials;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.CrudWorker;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Predicate;

public class LoginCollection implements CachedCrudWorker<Credentials, Set<Credentials>, String> {
  private Set<Credentials> cache;
  private final CrudWorker<Credentials, Set<Credentials>, String> dbworker;
  private final ReadWriteLock loginCollectionLock;

  public LoginCollection(
      CrudWorker<Credentials, Set<Credentials>, String> dbworker,
      ReadWriteLock loginCollectionLock) {
    this(dbworker, loginCollectionLock, new HashSet<>());
  }

  public LoginCollection(
      CrudWorker<Credentials, Set<Credentials>, String> dbworker,
      ReadWriteLock loginCollectionLock,
      Set<Credentials> cache) {
    this.dbworker = dbworker;
    this.cache = cache;
    this.loginCollectionLock = loginCollectionLock;
    setCache(this.dbworker.getAll());
  }

  @Override
  public void setCache(Set<Credentials> cache) {
    this.loginCollectionLock.writeLock().lock();
    try {
      this.cache = cache;
    } finally {
      this.loginCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public void clearCache() {
    this.loginCollectionLock.writeLock().lock();
    try {
      this.cache.clear();
    } finally {
      this.loginCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Optional<Credentials> add(Credentials obj, Predicate<Credentials> cond) {
    this.loginCollectionLock.writeLock().lock();
    try {
      Optional<Credentials> pair = dbworker.add(obj, cond);
      if (pair.isPresent()) {
        this.cache.add(pair.get());
      }
      return pair;
    } finally {
      this.loginCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Optional<Credentials> update(String key, Credentials obj, Predicate<Credentials> cond) {
    this.loginCollectionLock.writeLock().lock();
    try {
      Optional<Credentials> pair = dbworker.update(key, obj, cond);
      if (pair.isPresent()) {
        this.cache.removeIf(t -> t.getLogin().equals(key));
        this.cache.add(pair.get());
      }
      return pair;
    } finally {
      this.loginCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Optional<Credentials> get(String key) {
    this.loginCollectionLock.readLock().lock();
    try {
      return this.cache.stream().filter(pair -> pair.getLogin().equals(key)).findAny();
    } finally {
      this.loginCollectionLock.readLock().unlock();
    }
  }

  @Override
  public Set<Credentials> getAll() {
    this.loginCollectionLock.readLock().lock();
    try {
      return this.cache;
    } finally {
      this.loginCollectionLock.readLock().unlock();
    }
  }

  @Override
  public void remove(String key) {
    this.loginCollectionLock.writeLock().lock();
    try {
      Optional<Credentials> pair = get(key);
      if (pair.isPresent()) {
        this.dbworker.remove(key);
        this.cache.removeIf(login -> login.getLogin().equals(key));
      }
    } finally {
      this.loginCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public void clear() {
    this.loginCollectionLock.writeLock().lock();
    try {
      this.dbworker.clear();
      this.cache.clear();
    } finally {
      this.loginCollectionLock.writeLock().unlock();
    }
  }

  @Override
  public Iterator<Credentials> iterator() {
    this.loginCollectionLock.readLock().lock();
    try {
      if (this.cache == null) {
        return Collections.emptyIterator();
      }
      return this.cache.iterator();
    } finally {
      this.loginCollectionLock.readLock().unlock();
    }
  }
}
