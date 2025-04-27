package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.executor.commands.Command;

public interface ShellCommand extends Command<Void> {
  public ShellCommand setShell(TTY shell);

  public default boolean hasArgs() {
    return false;
  }
}
