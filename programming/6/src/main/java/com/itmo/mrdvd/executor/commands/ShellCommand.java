package com.itmo.mrdvd.executor.commands;

import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public interface ShellCommand extends Command {
  public Command setShell(Shell shell);

  public Optional<Shell> getShell();
}
