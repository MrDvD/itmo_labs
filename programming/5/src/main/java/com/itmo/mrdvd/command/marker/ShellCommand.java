package com.itmo.mrdvd.command.marker;

import java.util.Optional;

import com.itmo.mrdvd.shell.Shell;

public interface ShellCommand extends Command {
  public ShellCommand setShell(Shell<?, ?> shell);
  public Optional<Shell<?, ?>> getShell();
}
