package com.itmo.mrdvd.private_scope;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.commands.FetchAllCommand;
import com.itmo.mrdvd.commands.LoadCommand;
import com.itmo.mrdvd.commands.SaveCommand;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.util.HashMap;
import java.util.Map;

public class PrivateServerExecutor extends AbstractExecutor {
  public PrivateServerExecutor(
      Collection<Ticket, ?> collect,
      Mapper<? super TicketCollection, String> serial,
      Mapper<? extends TicketCollection, String> deserial,
      FileDescriptor fd,
      String path,
      Validator<Ticket> validator) {
    this(collect, serial, deserial, fd, path, validator, new HashMap<>(), new HashMap<>());
  }

  public PrivateServerExecutor(
      Collection<Ticket, ?> collect,
      Mapper<? super TicketCollection, String> serial,
      Mapper<? extends TicketCollection, String> deserial,
      FileDescriptor fd,
      String path,
      Validator<Ticket> validator,
      Map<String, Command<?>> commands,
      Map<String, Query> cachedQueries) {
    super(commands, cachedQueries);
    setCommand(new FetchAllCommand(this));
    setCommand(new SaveCommand(collect, serial, fd, path));
    setCommand(new LoadCommand(fd, collect, validator, deserial, path));
  }
}
