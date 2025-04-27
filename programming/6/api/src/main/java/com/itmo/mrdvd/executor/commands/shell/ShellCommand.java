package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.shell.Shell;

public interface ShellCommand extends Command<Void> {
  public ShellCommand setShell(Shell shell);

  public default boolean hasArgs() {
    return false;
  }
}
