package com.itmo.mrdvd.executor.command;

import java.util.Optional;

import com.itmo.mrdvd.shell.DefaultShell;

public class HelpCommand implements Command {
  private final DefaultShell<?, ?> shell;

  public HelpCommand() {
    this(null);
  }

  public HelpCommand(DefaultShell<?, ?> shell) {
    this.shell = shell;
  }

  @Override
  public HelpCommand setShell(DefaultShell<?, ?> shell) {
    return new HelpCommand(shell);
  }

  @Override
  public Optional<DefaultShell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    for (Command cmd : shell) {
      getShell()
          .get()
          .getOut()
          .write(String.format("%-35s\t%s\n", cmd.signature(), cmd.description()));
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
