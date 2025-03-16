package com.itmo.mrdvd.collection;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public interface IdGenerator {
  public Optional<Long> getId(Object obj);

  public void takeId(Long id) throws IllegalArgumentException;

  public void freeId(Long id);

  public boolean isTaken(Long id);
}
