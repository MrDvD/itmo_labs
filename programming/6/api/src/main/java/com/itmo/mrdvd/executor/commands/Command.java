package com.itmo.mrdvd.executor.commands;

public interface Command {
  public String name();

  public void execute() throws NullPointerException;
}
