package com.itmo.mrdvd.executor.commands;

import java.util.List;

public interface CommandWithParams<T> extends Command<T> {
  /** Passes parameters to the command for execution. */
  public CommandWithParams<T> withParams(List<?> params);
}
