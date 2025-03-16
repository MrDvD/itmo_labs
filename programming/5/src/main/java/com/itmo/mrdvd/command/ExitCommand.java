package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.command.marker.ShellCommand;
import com.itmo.mrdvd.shell.Shell;

public class ExitCommand implements ShellCommand {
  private Shell<?, ?, ?> shell;

  @Override
  public void execute() {
    if (shell != null) {
      shell.close();
    }
  }

  @Override
  public Optional<Shell<?, ?, ?>> getShell() {
   return Optional.ofNullable(this.shell);
  }

  @Override
  public void setShell(Shell<?, ?, ?> shell) {
    this.shell = shell;
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
