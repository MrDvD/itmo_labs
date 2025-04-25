package com.itmo.mrdvd.executor.commands;

public interface Command<T> {
  /** Returns the name of the command. */
  public String name();

  /** Executes the command. */
  public T execute() throws NullPointerException;
}
