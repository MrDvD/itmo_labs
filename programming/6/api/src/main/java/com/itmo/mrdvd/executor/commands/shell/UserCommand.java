package com.itmo.mrdvd.executor.commands.shell;

public interface UserCommand extends ShellCommand {
  public String signature();

  public String description();
}
