package com.itmo.mrdvd.collection;

import java.util.HashSet;
import java.util.Set;

public class TicketIdGenerator implements IdGenerator {
  Set<Long> usedIds;
  Set<Long> pendingIds;

  public TicketIdGenerator() {
    this.usedIds = new HashSet<Long>();
    this.pendingIds = new HashSet<Long>();
  }

  @Override
  public boolean isTaken(Long id) {
    return usedIds.contains(id);
  }

  @Override
  public boolean isBooked(Long id) {
    return pendingIds.contains(id);
  }

  @Override
  public Long bookId(Object obj) {
    if (obj == null) {
      return null;
    }
    Long newId = Math.abs(Long.valueOf(obj.hashCode()));
    while (isTaken(newId) || isBooked(newId) || newId == 0) {
      newId = Math.abs(newId + Math.round(Math.random() * 100000000000L - 50000000000L));
    }
    pendingIds.add(newId);
    return newId;
  }

  //  0: success
  // -1: id is already taken
  @Override
  public int takeId(Long id) {
    if (isTaken(id)) {
      return -1;
    }
    if (isBooked(id)) {
      pendingIds.remove(id);
    }
    usedIds.add(id);
    return 0;
  }

  //  0: success
  // -1: id does not exist
  // -2: incorrect id value
  @Override
  public int freeId(Long id) {
    if (id == null || id <= 0) {
      return -2;
    }
    if (!isTaken(id) || !isBooked(id)) {
      return -1;
    }
    usedIds.remove(id);
    pendingIds.remove(id);
    return 0;
  }
}
