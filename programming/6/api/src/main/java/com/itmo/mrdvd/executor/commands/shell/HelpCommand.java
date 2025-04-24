package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public class HelpCommand implements ShellCommand, UserCommand {
  private final Shell shell;

  public HelpCommand(Shell shell) {
    this.shell = shell;
  }

  @Override
  public HelpCommand setShell(Shell shell) {
    return new HelpCommand(shell);
  }

  @Override
  public Optional<Shell> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws IllegalStateException {
    if (getShell().isEmpty()) {
      throw new IllegalStateException("Не предоставлен интерпретатор для исполнения команды.");
    }
    for (String cmdName : this.shell.getShellCommandKeys()) {
      Optional<ShellCommand> cmd = this.shell.getCommand(cmdName);
      if (cmd.get() instanceof UserCommand userCmd) {
        getShell()
            .get()
            .getOut()
            .write(String.format("%-35s\t%s\n", userCmd.signature(), userCmd.description()));
      }
    }
    for (String queryName : this.shell.getQueryKeys()) {
      Optional<Query> q = this.shell.getQuery(queryName);
      getShell()
          .get()
          .getOut()
          .write(String.format("%-35s\t%s\n", q.get().getSignature(), q.get().getDesc()));
    }
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
    return "вывести справку по доступным командам";
  }
}
