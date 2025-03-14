package com.itmo.mrdvd.shell;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketComparator;
import com.itmo.mrdvd.command.AddCommand;
import com.itmo.mrdvd.command.AddIfMaxCommand;
import com.itmo.mrdvd.command.ClearCommand;
import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.command.CountGreaterThanEventCommand;
import com.itmo.mrdvd.command.ExecuteScriptCommand;
import com.itmo.mrdvd.command.ExitCommand;
import com.itmo.mrdvd.command.HelpCommand;
import com.itmo.mrdvd.command.InfoCommand;
import com.itmo.mrdvd.command.LoadCommand;
import com.itmo.mrdvd.command.MinByPriceCommand;
import com.itmo.mrdvd.command.PrintFieldDescendingTypeCommand;
import com.itmo.mrdvd.command.ReadEnvironmentFilepathCommand;
import com.itmo.mrdvd.command.RemoveAtCommand;
import com.itmo.mrdvd.command.RemoveByIdCommand;
import com.itmo.mrdvd.command.RemoveLastCommand;
import com.itmo.mrdvd.command.SaveCommand;
import com.itmo.mrdvd.command.ShellInfo;
import com.itmo.mrdvd.command.ShowCommand;
import com.itmo.mrdvd.command.UpdateCommand;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.object.TicketField;

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
      if (line == null || line.isBlank()) {
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

  public void initDefaultCommands(TicketCollection collection, String envName, FileDescriptor fd, Serializer<TicketCollection> serial, Deserializer<TicketCollection> deserial) {
    addCommand(new AddCommand(collection, getInput(), getOutput()));
    addCommand(new HelpCommand(getOutput()));
    addCommand(new ExitCommand());
    addCommand(new UpdateCommand(collection, getInput(), getOutput()));
    addCommand(new ClearCommand(collection, getOutput()));
    addCommand(new RemoveByIdCommand(collection, getOutput()));
    addCommand(new RemoveAtCommand(collection, getOutput()));
    addCommand(new RemoveLastCommand(collection, getOutput()));
    addCommand(new ShowCommand(collection, getOutput()));
    addCommand(new AddIfMaxCommand(collection, new TicketComparator(TicketField.ID), getInput(), getOutput()));
    addCommand(new MinByPriceCommand(collection, new TicketComparator(TicketField.PRICE), getOutput()));
    addCommand(new PrintFieldDescendingTypeCommand(collection, new TicketComparator(TicketField.TYPE, true), getOutput()));
    addCommand(new CountGreaterThanEventCommand(collection, getOutput()));
    addCommand(
        new ReadEnvironmentFilepathCommand(envName, fd, getOutput()), true);
    addCommand(new LoadCommand(fd, collection, deserial, getOutput()), true);
    addCommand(new SaveCommand(collection, serial, fd, getOutput()));
    addCommand(new ExecuteScriptCommand(getOutput(), fd));
    addCommand(new InfoCommand(collection, getOutput()));
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
      Optional<String> strCmd = getInput().read("> ");
      if (strCmd.isPresent()) {
         ProcessingStatus status = processCommandLine(strCmd.get());
         if (status.equals(ProcessingStatus.CMD_NOT_FOUND)) {
         getOutput()
               .writeln(
                  String.format(
                     "[ERROR] Не существует команды \"%s\".", getParser().parse(strCmd.get()).get().cmd()));
         }
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
