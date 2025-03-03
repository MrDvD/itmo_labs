package com.itmo.mrdvd.command;

import com.itmo.mrdvd.shell.Shell;

public class ExitCommand implements Command, ShellCommand {
  private Shell shell;

  @Override
  public void execute(String[] params) {
    if (shell != null) {
      shell.close();
    }
  }

  @Override
  public void setShell(Shell shell) {
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
