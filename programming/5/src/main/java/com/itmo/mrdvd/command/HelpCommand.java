package com.itmo.mrdvd.command;

import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.command.marker.ShellCommand;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public class HelpCommand implements ShellCommand {
  private final Shell<?, ?> shell;
  private final OutputDevice out;

  public HelpCommand(OutputDevice out) {
    this(out, null);
  }

  public HelpCommand(OutputDevice out, Shell<?, ?> shell) {
    this.out = out;
    this.shell = shell;
  }

  @Override
  public HelpCommand setShell(Shell<?, ?> shell) {
    return new HelpCommand(out, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() {
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
