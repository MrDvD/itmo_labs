package com.itmo.mrdvd.privateScope;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.commands.ClearCommand;
import com.itmo.mrdvd.commands.FetchAllCommand;
import com.itmo.mrdvd.commands.ShutdownCommand;
import com.itmo.mrdvd.service.Service;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import java.util.HashMap;
import java.util.Map;

public class PrivateServerExecutor extends AbstractExecutor {
  public <U> PrivateServerExecutor(
      Service server, CrudWorker<U, ?, ?> dbworker, CachedCrudWorker<U, ?, ?> collect) {
    this(server, dbworker, collect, new HashMap<>(), new HashMap<>());
  }

  public <U> PrivateServerExecutor(
      Service server,
      CrudWorker<U, ?, ?> dbworker,
      CachedCrudWorker<U, ?, ?> collect,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cache) {
    super(commands, cache);
    setCommand(new FetchAllCommand(this));
    setCommand(new ShutdownCommand(server));
    setCommand(new ClearCommand(collect));
  }
}
