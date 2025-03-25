package com.itmo.mrdvd.command.marker;

import java.util.Optional;

import com.itmo.mrdvd.shell.Shell;

public interface Command {
  public void execute() throws NullPointerException;

  public String name();

  public String signature();

  public String description();

  public Command setShell(Shell<?, ?> shell);

  public Optional<Shell<?, ?>> getShell();
}
