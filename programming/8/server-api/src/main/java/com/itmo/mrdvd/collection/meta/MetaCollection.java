package com.itmo.mrdvd.collection.meta;

import com.itmo.mrdvd.collection.AccessWorker;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;

public class MetaCollection implements AccessWorker<Map<String, Object>> {
  private final AccessWorker<Map<String, Object>> dbworker;
  private final ReadWriteLock metaLock;
  private Map<String, Object> cache;

  public MetaCollection(AccessWorker<Map<String, Object>> dbworker, ReadWriteLock metaLock) {
    this.dbworker = dbworker;
    this.metaLock = metaLock;
    this.metaLock.readLock().lock();
    try {
      this.dbworker
          .get()
          .ifPresent(
              (t) -> {
                this.cache = t;
              });
    } finally {
      this.metaLock.readLock().unlock();
    }
  }

  @Override
  public Optional<Map<String, Object>> get() {
    this.metaLock.readLock().lock();
    try {
      return Optional.ofNullable(this.cache);
    } finally {
      this.metaLock.readLock().unlock();
    }
  }

  @Override
  public void set(Map<String, Object> map) {
    this.metaLock.writeLock().lock();
    try {
      this.dbworker.set(map);
      this.cache = map;
    } finally {
      this.metaLock.writeLock().unlock();
    }
  }
}
