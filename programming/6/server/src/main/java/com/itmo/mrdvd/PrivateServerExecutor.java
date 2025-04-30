package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.commands.SaveCommand;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;

public class PrivateServerExecutor extends AbstractExecutor {
  public PrivateServerExecutor(Collection<?, ?> collect, Mapper<? super TicketCollection, String> serial, FileDescriptor fd) {
    this(collect, serial, fd, new HashMap<>(), new HashMap<>());
  }

  public PrivateServerExecutor(
      Collection<?, ?> collect, Mapper<? super TicketCollection, String> serial, FileDescriptor fd, Map<String, Command<?>> commands, Map<String, Query> cachedQueries) {
    super(commands, cachedQueries);
    setCommand(new SaveCommand(collect, serial, null));
  }
}