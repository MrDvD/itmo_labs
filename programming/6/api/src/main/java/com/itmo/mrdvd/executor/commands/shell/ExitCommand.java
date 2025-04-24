package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public class ExitCommand implements ShellCommand, UserCommand {
  private final Shell shell;

  public ExitCommand(Shell shell) {
    this.shell = shell;
  }

  @Override
  public void execute() throws IllegalStateException {
    if (getShell().isEmpty()) {
      throw new IllegalStateException("Не предоставлен интерпретатор для исполнения команды.");
    }
    getShell().get().close();
  }

  @Override
  public Optional<Shell> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public ExitCommand setShell(Shell shell) {
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
