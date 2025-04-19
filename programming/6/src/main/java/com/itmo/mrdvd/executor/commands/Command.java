package com.itmo.mrdvd.executor.commands;

public interface Command {
  public void execute() throws NullPointerException;

  public String name();

  public String signature();

  public String description();

  public default boolean hasParams() {
    return false;
  }
}
