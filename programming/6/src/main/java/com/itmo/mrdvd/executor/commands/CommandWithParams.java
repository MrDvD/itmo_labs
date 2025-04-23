package com.itmo.mrdvd.executor.commands;

import java.util.List;

public interface CommandWithParams extends Command {
  public CommandWithParams withParams(List<?> params);
}
