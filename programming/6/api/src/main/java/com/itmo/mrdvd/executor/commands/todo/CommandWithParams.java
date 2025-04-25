package com.itmo.mrdvd.executor.commands;

import java.util.List;

public interface CommandWithParams extends Command {
  /** Passes parameters to the command for execution. */
  public CommandWithParams withParams(List<?> params);
}
