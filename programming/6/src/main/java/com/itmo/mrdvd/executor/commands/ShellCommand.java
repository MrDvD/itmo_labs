package com.itmo.mrdvd.executor.commands;

import java.util.Optional;

import com.itmo.mrdvd.shell.Shell;

public interface ShellCommand extends Command {
  public Command setShell(Shell shell);

  public Optional<Shell> getShell();
}
