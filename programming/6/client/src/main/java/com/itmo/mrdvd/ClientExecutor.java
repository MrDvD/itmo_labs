package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.response.shell.FetchAllCommand;
import com.itmo.mrdvd.executor.commands.shell.ConnectCommand;
import com.itmo.mrdvd.executor.commands.shell.ExecuteScriptCommand;
import com.itmo.mrdvd.executor.commands.shell.ExitCommand;
import com.itmo.mrdvd.executor.commands.shell.HelpCommand;
import com.itmo.mrdvd.service.AbstractExecutor;

public class ClientExecutor extends AbstractExecutor {
  public ClientExecutor(DataFileDescriptor fd) {
    this(fd, new HashMap<>());
  }

  public ClientExecutor(DataFileDescriptor fd, Map<String, Command<?>> commands) {
    super(commands);
    setCommand(new HelpCommand(this));
    setCommand(new ExitCommand(this));
    setCommand(new ExecuteScriptCommand(fd, this));
    setCommand(new ConnectCommand(this, proxy));
    setCommand(new FetchAllCommand());
  }
}
