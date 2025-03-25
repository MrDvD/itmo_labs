package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.shell.Shell;

public class ExitCommand implements Command {
  private final Shell<?, ?> shell;

  public ExitCommand() {
    this(null);
  }

  public ExitCommand(Shell<?, ?> shell) {
    this.shell = shell;
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    getShell().get().close();
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public ExitCommand setShell(Shell<?, ?> shell) {
    return new ExitCommand(shell);
  }

  @Override
  public String name() {
    return "exit";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "завершить программу (без сохранения в файл)";
  }
}
