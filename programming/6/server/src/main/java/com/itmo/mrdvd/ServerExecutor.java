package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.builder.builders.SuppliedBuilder;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.commands.AddCommand;
import com.itmo.mrdvd.commands.FetchAllCommand;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;

public class ServerExecutor extends AbstractExecutor {
  public <U extends HavingId> ServerExecutor(Collection<U, ?> collect, SuppliedBuilder<U> builder) {
    this(collect, builder, new HashMap<>(), new HashMap<>());
  }

  public <U extends HavingId> ServerExecutor(
      Collection<U, ?> collect, SuppliedBuilder<U> builder, Map<String, Command<?>> commands, Map<String, Query> cachedQueries) {
    super(commands, cachedQueries);
    setCommand(new FetchAllCommand(this));
    setCommand(new AddCommand<>(collect, builder));
    // add the processing of errors in serverlistener!!!!!
  }
}
