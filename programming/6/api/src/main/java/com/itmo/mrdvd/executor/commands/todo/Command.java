package com.itmo.mrdvd.executor.commands;

public interface Command {
  /** Returns the name of the command. */
  public String name();

  /** Executes the command. */
  public void execute() throws NullPointerException;
}
