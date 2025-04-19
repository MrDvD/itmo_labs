package com.itmo.mrdvd.executor.commands;

import java.util.Optional;

import com.itmo.mrdvd.shell.Shell;

public class HelpCommand implements ShellCommand {
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
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    for (String cmdName : this.shell.getShellCommandKeys()) {
      Optional<Command> cmd = this.shell.getCommand(cmdName);
      getShell()
          .get()
          .getOut()
          .write(String.format("%-35s\t%s\n", cmd.get().signature(), cmd.get().description()));
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
