package com.itmo.mrdvd.shell;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.builder.examples.CoordinatesBuilder;
import com.itmo.mrdvd.builder.examples.CoordinatesUpdater;
import com.itmo.mrdvd.builder.examples.EventBuilder;
import com.itmo.mrdvd.builder.examples.EventUpdater;
import com.itmo.mrdvd.builder.examples.TicketBuilder;
import com.itmo.mrdvd.builder.examples.TicketUpdater;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketComparator;
import com.itmo.mrdvd.command.AddCommand;
import com.itmo.mrdvd.command.AddIfCommand;
import com.itmo.mrdvd.command.ClearCommand;
import com.itmo.mrdvd.command.CountGreaterThanEventCommand;
import com.itmo.mrdvd.command.ExecuteScriptCommand;
import com.itmo.mrdvd.command.ExitCommand;
import com.itmo.mrdvd.command.HelpCommand;
import com.itmo.mrdvd.command.InfoCommand;
import com.itmo.mrdvd.command.MinByPriceCommand;
import com.itmo.mrdvd.command.PrintFieldDescendingTypeCommand;
import com.itmo.mrdvd.command.ReadEnvironmentFilepathCommand;
import com.itmo.mrdvd.command.RemoveAtCommand;
import com.itmo.mrdvd.command.RemoveByIdCommand;
import com.itmo.mrdvd.command.RemoveLastCommand;
import com.itmo.mrdvd.command.SaveCommand;
import com.itmo.mrdvd.command.ShowCommand;
import com.itmo.mrdvd.command.UpdateCommand;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.command.marker.ShellCommand;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.device.input.InteractiveDataInputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class TicketShell extends Shell<Map<String, Command>, List<Command>, InteractiveDataInputDevice> {
  private boolean isOpen;

  public TicketShell(InteractiveDataInputDevice in, OutputDevice out, Map<String, Command> commands, List<Command> preExecute) {
    super(in, out, commands, preExecute); 
    this.isOpen = false;
  }

  public Optional<Command> addCommand(Command cmd, boolean preExec) {
    if (getCommands().containsKey(cmd.name())) {
      return Optional.empty();
    }
    if (cmd instanceof ShellCommand shellCmd) {
      shellCmd.setShell(this);
    }
    if (preExec) {
      getPreExecute().add(cmd);
    }
    getCommands().put(cmd.name(), cmd);
    return Optional.of(cmd);
  }

  public void initDefaultCommands(TicketCollection collection, String envName, FileDescriptor fd, Serializer<Collection<Ticket,List<Ticket>>> serial, Deserializer<Collection<Ticket,List<Ticket>>> deserial) {
    addCommand(new AddCommand<>(collection, new TicketBuilder(new CoordinatesBuilder(getInput(), getOutput()), new EventBuilder(getInput(), getOutput()), getInput(), getOutput()), getOutput()));
    addCommand(new HelpCommand(getOutput()));
    addCommand(new ExitCommand());
    addCommand(new UpdateCommand<>(collection, new TicketUpdater(new CoordinatesUpdater(getInput(), getOutput()), new EventUpdater(getInput(), getOutput()), getInput(), getOutput()), getInput(), getOutput()));
    addCommand(new ClearCommand(collection, getOutput()));
    addCommand(new RemoveByIdCommand(collection, getInput(), getOutput()));
    addCommand(new RemoveAtCommand<>(collection, getInput(), getOutput()));
    addCommand(new RemoveLastCommand<>(collection, getOutput()));
    addCommand(new ShowCommand(collection, getOutput()));
    addCommand(new AddIfCommand<>(collection, new TicketBuilder(new CoordinatesBuilder(getInput(), getOutput()), new EventBuilder(getInput(), getOutput()), getInput(), getOutput()), new TicketComparator(TicketField.ID), Set.of(1), getOutput()));
    addCommand(new MinByPriceCommand<>(collection, new TicketComparator(TicketField.PRICE), getOutput()));
    addCommand(new PrintFieldDescendingTypeCommand<>(collection, new TicketComparator(TicketField.TYPE, true), getOutput()));
    addCommand(new CountGreaterThanEventCommand(collection, getInput(), getOutput()));
    addCommand(
        new ReadEnvironmentFilepathCommand(envName, fd, getOutput()), true);
   //  addCommand(new LoadCommand<>(fd, collection, deserial, getOutput()), true);
    addCommand(new SaveCommand<>(collection, serial, fd, getOutput()));
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
      cmd.execute();
    }
    this.isOpen = true;
    while (this.isOpen) {
      getInput().write("> ");
         Optional<Command> cmd = processCommandLine();
         if (cmd.isEmpty()) {
            getOutput().writeln("[ERROR] Команда не найдена: введите 'help' для просмотра списка доступных команд.");
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
