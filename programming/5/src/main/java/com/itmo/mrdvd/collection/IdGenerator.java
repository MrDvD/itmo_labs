package com.itmo.mrdvd.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public interface IdGenerator {
  public Long bookId(Object obj);

  public int takeId(Long id);

  public int freeId(Long id);

  public boolean isTaken(Long id);

  public boolean isBooked(Long id);
}
