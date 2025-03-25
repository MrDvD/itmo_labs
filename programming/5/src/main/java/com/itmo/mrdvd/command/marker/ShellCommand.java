package com.itmo.mrdvd.command.marker;

import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public interface ShellCommand extends Command {
  public ShellCommand setShell(Shell<?, ?> shell);

  public Optional<Shell<?, ?>> getShell();
}
