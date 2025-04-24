package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.executor.Executor;
import com.itmo.mrdvd.executor.commands.shell.ConnectCommand;
import com.itmo.mrdvd.executor.commands.shell.ExecuteScriptCommand;
import com.itmo.mrdvd.executor.commands.shell.ExitCommand;
import com.itmo.mrdvd.executor.commands.shell.HelpCommand;
import com.itmo.mrdvd.executor.commands.shell.ProcessQueryCommand;
import com.itmo.mrdvd.executor.commands.shell.ShellCommand;
import com.itmo.mrdvd.executor.queries.FetchAllQuery;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;
import java.util.HashMap;
import java.util.Map;

public class CollectionShell extends ProxyShell {
  public CollectionShell(
      Executor exec,
      ClientProxy proxy,
      DataInputDevice in,
      OutputDevice out,
      DataFileDescriptor fd) {
    this(exec, proxy, in, out, fd, new HashMap<>(), new HashMap<>());
  }

  public CollectionShell(
      Executor exec,
      ClientProxy proxy,
      DataInputDevice in,
      OutputDevice out,
      DataFileDescriptor fd,
      Map<String, Query> cachedQueries,
      Map<String, ShellCommand> shellCommands) {
    super(exec, in, out, cachedQueries, shellCommands);
    setCommand(new HelpCommand(this));
    setCommand(new ExitCommand(this));
    setCommand(new ExecuteScriptCommand(fd, this));
    setCommand(new ProcessQueryCommand(this, proxy, exec));
    setCommand(new ConnectCommand(this, proxy));
    setQuery(new FetchAllQuery());
  }
}
