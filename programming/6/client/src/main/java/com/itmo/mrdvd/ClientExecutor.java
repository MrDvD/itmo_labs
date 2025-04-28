package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.shell.ConnectCommand;
import com.itmo.mrdvd.executor.commands.shell.ExecuteScriptCommand;
import com.itmo.mrdvd.executor.commands.shell.ExitCommand;
import com.itmo.mrdvd.executor.commands.shell.HelpCommand;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.shell.FetchAllCommand;
import com.itmo.mrdvd.service.AbstractExecutor;
import com.itmo.mrdvd.service.AbstractSender;

public class ClientExecutor extends AbstractExecutor {
  public ClientExecutor(DataFileDescriptor fd, AbstractSender<?, ?, ?> sender) {
    this(fd, sender, new HashMap<>(), new HashMap<>());
  }

  public ClientExecutor(DataFileDescriptor fd, AbstractSender<?, ?, ?> sender, Map<String, Command<?>> commands, Map<String, Query> cachedQueries) {
    super(commands, cachedQueries);
    setCommand(new HelpCommand(this));
    setCommand(new ExitCommand());
    setCommand(new ExecuteScriptCommand(fd));
    setCommand(new ConnectCommand(sender));
    // setCommand(new FetchAllCommand());
  }
}
