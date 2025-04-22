package com.itmo.mrdvd.shell;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.executor.commands.ExecuteScriptCommand;
import com.itmo.mrdvd.executor.commands.ExitCommand;
import com.itmo.mrdvd.executor.commands.HelpCommand;
import com.itmo.mrdvd.executor.commands.ShellCommand;
import com.itmo.mrdvd.executor.queries.FetchAllQuery;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;

public class CollectionShell extends ProxyShell {
  public CollectionShell(
      ClientProxy proxy, DataInputDevice in, OutputDevice out, DataFileDescriptor fd) {
    this(proxy, in, out, fd, new HashMap<>(), new HashMap<>());
  }

  public CollectionShell(
      ClientProxy proxy,
      DataInputDevice in,
      OutputDevice out,
      DataFileDescriptor fd,
      Map<String, Query> cachedQueries,
      Map<String, ShellCommand> shellCommands) {
    super(proxy, in, out, cachedQueries, shellCommands);
    setCommand(new HelpCommand(this));
    setCommand(new ExitCommand(this));
    setCommand(new ExecuteScriptCommand(fd, this));
    setQuery(new FetchAllQuery());
  }
}
