package com.itmo.mrdvd.executor.commands;

public interface Command<T> {
  /** Returns the name of the command. */
  public String name();

  public String signature();

  public String description();

  /** Executes the command. */
  public T execute(Object params) throws IllegalArgumentException;
}
