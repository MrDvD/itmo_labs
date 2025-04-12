package com.itmo.mrdvd.executor.command;

import java.util.Optional;

import com.itmo.mrdvd.shell.DefaultShell;

public interface Command {
  public void execute() throws NullPointerException;

  public String name();

  public String signature();

  public String description();

  public Command setShell(DefaultShell<?, ?> shell);

  public Optional<DefaultShell<?, ?>> getShell();

  public default boolean hasParams() {
    return false;
  }
}
