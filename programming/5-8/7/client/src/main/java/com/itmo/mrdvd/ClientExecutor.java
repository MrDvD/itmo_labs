package com.itmo.mrdvd;

import com.itmo.mrdvd.commands.ConnectCommand;
import com.itmo.mrdvd.commands.ExecuteScriptCommand;
import com.itmo.mrdvd.commands.ExitCommand;
import com.itmo.mrdvd.commands.FetchAllCommandMeta;
import com.itmo.mrdvd.commands.HelpCommand;
import com.itmo.mrdvd.commands.LoginCommand;
import com.itmo.mrdvd.commands.RegisterCommandMeta;
import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.AuthContext;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import java.util.HashMap;
import java.util.Map;

public class ClientExecutor extends AbstractExecutor {
  public ClientExecutor(
      DataFileDescriptor fd, AbstractSender<?> sender, AuthContext<LoginPasswordPair> authContext) {
    this(fd, sender, authContext, new HashMap<>(), new HashMap<>());
  }

  public ClientExecutor(
      DataFileDescriptor fd,
      AbstractSender<?> sender,
      AuthContext<LoginPasswordPair> authContext,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cachedCommands) {
    super(commands, cachedCommands);
    setCommand(new HelpCommand(this));
    setCommand(new ExitCommand());
    setCommand(new ExecuteScriptCommand(fd));
    setCommand(new ConnectCommand(sender));
    setCommand(new LoginCommand(authContext));
    setCache(new FetchAllCommandMeta());
    setCache(new RegisterCommandMeta());
  }
}
