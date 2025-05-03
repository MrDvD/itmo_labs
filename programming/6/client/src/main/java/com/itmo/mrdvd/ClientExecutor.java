package com.itmo.mrdvd;

import com.itmo.mrdvd.commands.ConnectCommand;
import com.itmo.mrdvd.commands.ExecuteScriptCommand;
import com.itmo.mrdvd.commands.ExitCommand;
import com.itmo.mrdvd.commands.HelpCommand;
import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.queries.FetchAllQuery;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import java.util.HashMap;
import java.util.Map;

public class ClientExecutor extends AbstractExecutor {
  public ClientExecutor(DataFileDescriptor fd, AbstractSender<?, ?, ?> sender) {
    this(fd, sender, new HashMap<>(), new HashMap<>());
  }

  public ClientExecutor(
      DataFileDescriptor fd,
      AbstractSender<?, ?, ?> sender,
      Map<String, Command<?>> commands,
      Map<String, Query> cachedQueries) {
    super(commands, cachedQueries);
    setCommand(new HelpCommand(this));
    setCommand(new ExitCommand());
    setCommand(new ExecuteScriptCommand(fd));
    setCommand(new ConnectCommand(sender));
    setQuery(new FetchAllQuery());
  }
}
