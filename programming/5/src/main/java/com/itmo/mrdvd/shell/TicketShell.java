package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.collection.TicketCollection;
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
import com.itmo.mrdvd.command.ShowCommand;
import com.itmo.mrdvd.command.UpdateCommand;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class TicketShell implements Shell {
  private final InteractiveInputDevice in;
  private final OutputDevice out;
  private final Map<String, Command> commands;
  private final ArrayList<Command> preExecute;
  private boolean isOpen;
  private int stackSize;

  public TicketShell(
      InteractiveInputDevice in,
      OutputDevice out,
      TicketCollection collection,
      Serializer<TicketCollection> serial,
      Deserializer<TicketCollection> deserial) {
    this.in = in;
    this.out = out;
    this.commands = new TreeMap<>();
    this.preExecute = new ArrayList<>();
    this.isOpen = false;
    this.stackSize = 256;
    initCommands(in, out, collection, serial, deserial, "TICKET_TEST");
  }

  private void initCommands(
      InteractiveInputDevice in,
      OutputDevice out,
      TicketCollection collection,
      Serializer<TicketCollection> serial,
      Deserializer<TicketCollection> deserial,
      String envName) {
    Command add = new AddCommand(collection, in, out);
    commands.put(add.name(), add);
    Command help = new HelpCommand(this, out);
    commands.put(help.name(), help);
    Command exit = new ExitCommand(this);
    commands.put(exit.name(), exit);
    Command update = new UpdateCommand(collection, in, out);
    commands.put(update.name(), update);
    Command clear = new ClearCommand(collection, out);
    commands.put(clear.name(), clear);
    Command removeById = new RemoveByIdCommand(collection, out);
    commands.put(removeById.name(), removeById);
    Command removeAt = new RemoveAtCommand(collection, out);
    commands.put(removeAt.name(), removeAt);
    Command removeLast = new RemoveLastCommand(collection, out);
    commands.put(removeLast.name(), removeLast);
    Command showCommand = new ShowCommand(collection, out);
    commands.put(showCommand.name(), showCommand);
    Command addIfMaxCommand = new AddIfMaxCommand(collection, in, out);
    commands.put(addIfMaxCommand.name(), addIfMaxCommand);
    Command minByPriceCommand = new MinByPriceCommand(collection, out);
    commands.put(minByPriceCommand.name(), minByPriceCommand);
    Command printFieldDescendingTypeCommand = new PrintFieldDescendingTypeCommand(collection, out);
    commands.put(printFieldDescendingTypeCommand.name(), printFieldDescendingTypeCommand);
    Command countGreaterThanEventCommand = new CountGreaterThanEventCommand(collection, out);
    commands.put(countGreaterThanEventCommand.name(), countGreaterThanEventCommand);
    FileDescriptor fd = createFd();
    Command loadCommand = new LoadCommand(fd, collection, deserial, out);
    commands.put(loadCommand.name(), loadCommand);
    Command readEnvironmentFilepathCommand = new ReadEnvironmentFilepathCommand(envName, fd, out);
    commands.put(readEnvironmentFilepathCommand.name(), readEnvironmentFilepathCommand);
    Command saveCommand = new SaveCommand(collection, serial, fd, out);
    commands.put(saveCommand.name(), saveCommand);
    Command executeScriptCommand = new ExecuteScriptCommand(this, out);
    commands.put(executeScriptCommand.name(), executeScriptCommand);
    Command infoCommand = new InfoCommand(collection, out);
    commands.put(infoCommand.name(), infoCommand);

    preExecute.add(readEnvironmentFilepathCommand);
    preExecute.add(loadCommand);
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
  public FileDescriptor createFd() {
    return new FileIO();
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
  public int processCommandLine(String line) {
    RawCommand rawCmd = TShellParser.parseLine(line);
    if (rawCmd == null) {
      return -2;
    }
    if (getCommands().containsKey(rawCmd.cmd)) {
      getCommands().get(rawCmd.cmd).execute(rawCmd.params);
      return 0;
    } else {
      return -1;
    }
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
