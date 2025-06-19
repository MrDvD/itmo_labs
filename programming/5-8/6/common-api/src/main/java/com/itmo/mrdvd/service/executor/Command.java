package com.itmo.mrdvd.service.executor;

import java.util.List;

public interface Command<T> {
  /** Returns the name of the command. */
  public String name();

  public String signature();

  public String description();

  /** Executes the command. */
  public T execute(List<Object> params) throws IllegalArgumentException;
}
