package com.itmo.mrdvd.server;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.executor.DefaultExecutor;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.collectioncmds.AddCommand;
import java.util.HashMap;
import java.util.Map;

public class ServerExecutor extends DefaultExecutor {
  public <U extends HavingId> ServerExecutor(
      Collection<U, ?> collect, InteractiveBuilder<U, ?> builder) {
    this(collect, builder, new HashMap<>());
  }

  public <U extends HavingId> ServerExecutor(
      Collection<U, ?> collect, InteractiveBuilder<U, ?> builder, Map<String, Command> commands) {
    super(commands);
    setCommand(new AddCommand<>(collect, builder));
  }
}
