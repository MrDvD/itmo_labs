package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import java.util.List;
import java.util.Optional;

/** Propably do it as a method of AbstractShell. */
public class HelpCommand implements Command<String> {
  private final AbstractExecutor exec;

  public HelpCommand(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public String execute(List<Object> params) {
    String result = "Available local commands:\n";
    for (String cmdName : this.exec.getCommandKeys()) {
      Optional<Command<?>> cmd = this.exec.getCommand(cmdName);
      if (cmd.isPresent()) {
        result += String.format("%-35s\t%s\n", cmd.get().signature(), cmd.get().description());
      }
    }
    result += "\nKnown cached queries:\n";
    for (String cmdName : this.exec.getCachedKeys()) {
      Optional<CommandMeta> cmd = this.exec.getCache(cmdName);
      if (cmd.isPresent()) {
        result += String.format("%-35s\t%s\n", cmd.get().getSignature(), cmd.get().getDesc());
      }
    }
    return result;
  }

  @Override
  public String name() {
    return "help";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести справку по доступным командам и запросам";
  }
}
