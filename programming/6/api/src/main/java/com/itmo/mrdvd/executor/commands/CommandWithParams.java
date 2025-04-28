package com.itmo.mrdvd.executor.commands;

public interface CommandWithParams<T> extends Command<T> {
  /** Passes parameters to the command for execution. */
  public CommandWithParams<T> withParams(Object params);
}
