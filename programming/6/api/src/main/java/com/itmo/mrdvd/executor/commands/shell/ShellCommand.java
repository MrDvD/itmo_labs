package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public interface ShellCommand extends Command {
  public Command setShell(Shell shell);

  public Optional<Shell> getShell();

  public default boolean hasParams() {
    return false;
  }
}
