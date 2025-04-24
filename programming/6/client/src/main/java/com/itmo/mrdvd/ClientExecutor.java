package com.itmo.mrdvd;

import com.itmo.mrdvd.executor.DefaultExecutor;
import com.itmo.mrdvd.executor.commands.shell.execute.FetchAllCommand;

public class ClientExecutor extends DefaultExecutor {
  public ClientExecutor() {
    setCommand(new FetchAllCommand());
  }
}
