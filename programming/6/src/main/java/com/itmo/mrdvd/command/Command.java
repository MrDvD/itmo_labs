package com.itmo.mrdvd.command;

import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public interface Command {
  public void execute() throws NullPointerException;

  public String name();

  public String signature();

  public String description();

  public Command setShell(Shell<?, ?> shell);

  public Optional<Shell<?, ?>> getShell();

  public default boolean hasParams() {
    return false;
  }
}
