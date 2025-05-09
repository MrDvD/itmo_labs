package com.itmo.mrdvd.private_scope;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.commands.FetchAllCommand;
import com.itmo.mrdvd.commands.LoadCommand;
import com.itmo.mrdvd.commands.SaveCommand;
import com.itmo.mrdvd.commands.ShutdownCommand;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.AbstractListener;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import com.itmo.mrdvd.validators.Validator;
import java.util.HashMap;
import java.util.Map;

public class PrivateServerExecutor extends AbstractExecutor {
  public PrivateServerExecutor(
      AbstractListener<?> server,
      Collection<Ticket, ?> collect,
      Mapper<? super TicketCollection, String> serial,
      Mapper<String, ? extends TicketCollection> deserial,
      FileDescriptor fd,
      String path,
      Validator<Ticket> validator) {
    this(server, collect, serial, deserial, fd, path, validator, new HashMap<>(), new HashMap<>());
  }

  public PrivateServerExecutor(
      AbstractListener<?> server,
      Collection<Ticket, ?> collect,
      Mapper<? super TicketCollection, String> serial,
      Mapper<String, ? extends TicketCollection> deserial,
      FileDescriptor fd,
      String path,
      Validator<Ticket> validator,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cache) {
    super(commands, cache);
    setCommand(new FetchAllCommand(this));
    setCommand(new SaveCommand(collect, serial, fd, path));
    setCommand(new LoadCommand(fd, collect, validator, deserial, path));
    setCommand(new ShutdownCommand(server));
  }
}
