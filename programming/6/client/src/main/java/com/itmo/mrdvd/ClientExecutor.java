package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.executor.AbstractExecutor;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.response.shell.FetchAllCommand;

public class ClientExecutor extends AbstractExecutor {
  public ClientExecutor() {
    this(new HashMap<>());
  }

  public ClientExecutor(Map<String, Command<?>> commands) {
    super(commands);
    Command<?> fetchAll = new FetchAllCommand();
    getCommands().put(fetchAll.name(), fetchAll);
  }
}
