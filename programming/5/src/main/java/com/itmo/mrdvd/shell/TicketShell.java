package com.itmo.mrdvd.shell;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.command.ShellInfo;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;

public class TicketShell extends Shell<Map<String, Command>, List<Command>> {
  private boolean isOpen;

  public TicketShell(InteractiveInputDevice in, OutputDevice out, Map<String, Command> commands, List<Command> preExecute) {
   this(in, out, commands, preExecute, new TShellParser()); 
  }

  public TicketShell(InteractiveInputDevice in, OutputDevice out, Map<String, Command> commands, List<Command> preExecute, ShellParser parser) {
   super(in, out, commands, preExecute, parser); 
    this.isOpen = false;
  }

  public static class TShellParser implements ShellParser {
   public static class TShellQuery extends ShellQuery {
      public TShellQuery(String cmd, String[] params) {
         super(cmd, params);
      }
     }
   @Override
    public Optional<ShellQuery> parse(String line) {
      if (line.isBlank()) {
        return Optional.empty();
      }
      String[] keys = line.split(" ");
      ShellQuery rawCmd = new TShellQuery(keys[0], Arrays.copyOfRange(keys, 1, keys.length));
      return Optional.of(rawCmd);
    }
  }

  public Optional<Command> addCommand(Command cmd, boolean preExec) {
    if (getCommands().containsKey(cmd.name())) {
      return Optional.empty();
    }
    if (cmd instanceof ShellInfo shellCmd) {
      shellCmd.setShell(this);
    }
    if (preExec) {
      getPreExecute().add(cmd);
    }
    getCommands().put(cmd.name(), cmd);
    return Optional.of(cmd);
  }

  @Override
  public Optional<Command> addCommand(Command cmd) {
    return addCommand(cmd, false);
  }

  @Override
  public void open() {
    for (Command cmd : getPreExecute()) {
      cmd.execute(null);
    }
    this.isOpen = true;
    while (this.isOpen) {
      String strCmd = getInput().read("> ");
      ProcessingStatus status = processCommandLine(strCmd);
      if (status.equals(ProcessingStatus.CMD_NOT_FOUND)) {
        getOutput()
            .writeln(
                String.format(
                    "[ERROR] Не существует команды \"%s\".", getParser().parse(strCmd).get().cmd()));
      }
    }
  }

  @Override
  public Optional<Command> getCommand(String line) {
    return Optional.ofNullable(getCommands().get(line));
  }

  @Override
  public void close() {
    this.isOpen = false;
  }

  @Override
  public Iterator<Command> iterator() {
   return getCommands().values().iterator();
  }
}
