package com.itmo.mrdvd.shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.command.ShellCommand;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;

public class TicketShell implements Shell {
  private final InteractiveInputDevice in;
  private final OutputDevice out;
  private final Map<String, Command> commands;
  private final ArrayList<Command> preExecute;
  private boolean isOpen;
  private int stackSize;

  public TicketShell(InteractiveInputDevice in, OutputDevice out) {
    this.in = in;
    this.out = out;
    this.commands = new TreeMap<>();
    this.preExecute = new ArrayList<>();
    this.isOpen = false;
    this.stackSize = 256;
  }

  public static class RawCommand {
    String cmd;
    String[] params;

    public RawCommand(int paramsCount) {
      params = new String[paramsCount];
    }

    @Override
    public int hashCode() {
      return cmd.hashCode();
    }

    @Override
    public boolean equals(Object other) {
      if (other == null || !(other instanceof RawCommand)) {
        return false;
      }
      RawCommand otherCmd = (RawCommand) other;
      return this.cmd.equals(otherCmd.cmd);
    }
  }

  public static class TShellParser {
    public static RawCommand parseLine(String line) {
      if (line.isBlank()) {
        return null;
      }
      String[] keys = line.split(" ");
      RawCommand rawCmd = new RawCommand(keys.length - 1);
      rawCmd.cmd = keys[0];
      rawCmd.params = Arrays.copyOfRange(keys, 1, keys.length);
      return rawCmd;
    }
  }

  public int addCommand(Command cmd, boolean preExec) {
    if (commands.containsKey(cmd.name())) {
      return -1;
    }
    if (cmd instanceof ShellCommand shellCmd) {
      shellCmd.setShell(this);
    }
    commands.put(cmd.name(), cmd);
    if (preExec) {
      preExecute.add(cmd);
    }
    return 0;
  }

  @Override
  public int addCommand(Command cmd) {
    return addCommand(cmd, false);
  }

  @Override
  public void open() {
    for (Command cmd : preExecute) {
      cmd.execute(null);
    }
    this.isOpen = true;
    while (this.isOpen) {
      String strCmd = getInput().read("> ");
      int code = processCommandLine(strCmd);
      if (code == -1) {
        getOutput()
            .writeln(
                String.format(
                    "[ERROR] Не существует команды \"%s\".", TShellParser.parseLine(strCmd).cmd));
      }
    }
  }

  @Override
  public void setStackSize(int size) {
    this.stackSize = size;
  }

  @Override
  public int getStackSize() {
    return this.stackSize;
  }

  @Override
  public Command getCommand(String line) {
    return getCommands().get(line);
  }

  @Override
  public int processCommandLine(String line) {
    RawCommand rawCmd = TShellParser.parseLine(line);
    if (rawCmd == null) {
      return -2;
    }
    Command cmd = getCommand(rawCmd.cmd);
    if (cmd != null) {
      cmd.execute(rawCmd.params);
      return 0;
    }
    return -1;
  }

  @Override
  public InteractiveInputDevice getInput() {
    return this.in;
  }

  @Override
  public OutputDevice getOutput() {
    return this.out;
  }

  @Override
  public void close() {
    this.isOpen = false;
  }

  @Override
  public Map<String, Command> getCommands() {
    return commands;
  }
}
