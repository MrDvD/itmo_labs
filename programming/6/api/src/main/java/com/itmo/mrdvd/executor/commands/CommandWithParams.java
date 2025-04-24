package com.itmo.mrdvd.executor.commands;

import java.util.List;

public interface CommandWithParams extends Command {
  /** Passes the parameters to the command. */
  public CommandWithParams withParams(List<?> params);
}
