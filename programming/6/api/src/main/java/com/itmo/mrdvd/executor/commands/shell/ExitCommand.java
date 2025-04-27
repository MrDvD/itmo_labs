package com.itmo.mrdvd.executor.commands.shell;

import java.util.Optional;

import com.itmo.mrdvd.device.TTY;

public class ExitCommand implements UserCommand {
  private final TTY shell;

  public ExitCommand(TTY shell) {
    this.shell = shell;
  }

  @Override
  public Void execute() throws IllegalStateException {
    if (getShell().isEmpty()) {
      throw new IllegalStateException("Не предоставлен интерпретатор для исполнения команды.");
    }
    getShell().get().close();
    return null;
  }

  @Override
  public Optional<TTY> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public ExitCommand setShell(TTY shell) {
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
    return "завершить интерпретатор (без сохранения состояния)";
  }
}
