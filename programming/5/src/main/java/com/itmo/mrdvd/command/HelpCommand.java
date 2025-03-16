package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.shell.Shell;

public class HelpCommand implements Command, ShellInfo {
  private Shell<?, ?, ?> shell;
  private final OutputDevice out;

  public HelpCommand(OutputDevice out) {
    this.out = out;
  }

  @Override
  public void setShell(Shell<?, ?, ?> shell) {
    this.shell = shell;
  }

  @Override
  public Optional<Shell<?, ?, ?>> getShell() {
   return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute(String[] params) {
    if (shell != null) {
      for (Command cmd : shell) {
        out.write(String.format("%-35s\t%s\n", cmd.signature(), cmd.description()));
      }
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
