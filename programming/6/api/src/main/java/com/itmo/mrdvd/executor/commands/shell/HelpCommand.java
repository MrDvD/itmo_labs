package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.service.AbstractExecutor;
import java.util.Optional;

/** Propably do it as a method of AbstractShell. */
public class HelpCommand implements Command<String> {
  private final AbstractExecutor exec;

  public HelpCommand(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public String execute(Object params) {
    String result = "Available local commands:\n";
    for (String cmdName : this.exec.getCommandKeys()) {
      Optional<Command<?>> cmd = this.exec.getCommand(cmdName);
      if (cmd.isPresent()) {
        result += String.format("%-35s\t%s\n", cmd.get().signature(), cmd.get().description());
      }
    }
    result += "\nKnown cached queries:\n";
    for (String cmdName : this.exec.getCachedQueryKeys()) {
      Optional<Query> cmd = this.exec.getQuery(cmdName);
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
