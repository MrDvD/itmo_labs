package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.builder.builders.SuppliedBuilder;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.executor.DefaultExecutor;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.collection.AddCommand;

public class ServerExecutor extends DefaultExecutor {
  public <U extends HavingId> ServerExecutor(Collection<U, ?> collect, SuppliedBuilder<U> builder) {
    this(collect, builder, new HashMap<>());
  }

  public <U extends HavingId> ServerExecutor(
      Collection<U, ?> collect, SuppliedBuilder<U> builder, Map<String, Command> commands) {
    super(commands);
    setCommand(new AddCommand<>(collect, builder));
  }
}
