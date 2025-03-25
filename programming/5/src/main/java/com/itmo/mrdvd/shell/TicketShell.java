package com.itmo.mrdvd.shell;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.builder.builders.InteractiveCoordinatesBuilder;
import com.itmo.mrdvd.builder.builders.InteractiveEventBuilder;
import com.itmo.mrdvd.builder.builders.InteractiveTicketBuilder;
import com.itmo.mrdvd.builder.updaters.InteractiveCoordinatesUpdater;
import com.itmo.mrdvd.builder.updaters.InteractiveEventUpdater;
import com.itmo.mrdvd.builder.updaters.InteractiveTicketUpdater;
import com.itmo.mrdvd.builder.validators.CoordinatesValidator;
import com.itmo.mrdvd.builder.validators.EventValidator;
import com.itmo.mrdvd.builder.validators.TicketValidator;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketComparator;
import com.itmo.mrdvd.command.AddCommand;
import com.itmo.mrdvd.command.AddIfCommand;
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
import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class TicketShell extends Shell<Map<String, Command>, List<Command>> {
  private boolean isOpen;

  public TicketShell(DataInputDevice in, OutputDevice out) {
    this(in, out, new HashMap<>(), new ArrayList<>());
  }

  public TicketShell(
      DataInputDevice in,
      OutputDevice out,
      Map<String, Command> commands,
      List<Command> preExecute) {
    super(in, out, commands, preExecute);
    this.isOpen = false;
  }

  public Optional<Command> addCommand(Command cmd, boolean preExec) {
    if (getCommands().containsKey(cmd.name())) {
      return Optional.empty();
    }
    cmd = cmd.setShell(this);
    if (getPreExecute().isPresent() && preExec) {
      getPreExecute().get().add(cmd);
    }
    getCommands().put(cmd.name(), cmd);
    return Optional.of(cmd);
  }

  public void initDefaultCommands(
      TicketCollection collection,
      String envName,
      DataFileDescriptor fd,
      Serializer<Collection<Ticket, List<Ticket>>> serial,
      Deserializer<Collection<Ticket, List<Ticket>>> deserial,
      Set<Path> usedPaths) {
    addCommand(
        new AddCommand<>(
            collection,
            new InteractiveTicketBuilder(
                new InteractiveCoordinatesBuilder(this::getIn, getOut()),
                new InteractiveEventBuilder(this::getIn, getOut()),
                this::getIn,
                getOut())));
    addCommand(new HelpCommand());
    addCommand(new ExitCommand());
    addCommand(
        new UpdateCommand<>(
            collection,
            new InteractiveTicketUpdater(
                new InteractiveCoordinatesUpdater(this::getIn, getOut()),
                new InteractiveEventUpdater(this::getIn, getOut()),
                this::getIn,
                getOut())));
    addCommand(new ClearCommand(collection));
    addCommand(new RemoveByIdCommand(collection, getIn(), getOut()));
    addCommand(new RemoveAtCommand<>(collection, getIn(), getOut()));
    addCommand(new RemoveLastCommand<>(collection, getOut()));
    addCommand(new ShowCommand(collection));
    addCommand(
        new AddIfCommand<>(
            collection,
            new InteractiveTicketBuilder(
                new InteractiveCoordinatesBuilder(() -> this.getIn(), getOut()),
                new InteractiveEventBuilder(() -> this.getIn(), getOut()),
                () -> this.getIn(),
                getOut()),
            new TicketComparator(TicketField.ID),
            Set.of(1)));
    addCommand(
        new MinByPriceCommand<>(collection, new TicketComparator(TicketField.PRICE)));
    addCommand(
        new PrintFieldDescendingTypeCommand<>(
            collection, new TicketComparator(TicketField.TYPE, true)));
    addCommand(new CountGreaterThanEventCommand(collection));
    addCommand(new ReadEnvironmentFilepathCommand(envName, fd), true);
    addCommand(
        new LoadCommand<>(
            fd,
            collection,
            new TicketValidator(new CoordinatesValidator(), new EventValidator()),
            deserial),
        true);
    addCommand(new SaveCommand<>(collection, serial, fd));
    addCommand(new ExecuteScriptCommand(fd, usedPaths));
    addCommand(new InfoCommand(collection));
  }

  @Override
  public Optional<Command> addCommand(Command cmd) {
    return addCommand(cmd, false);
  }

  @Override
  public void open() {
    if (getPreExecute().isPresent()) {
      for (Command cmd : getPreExecute().get()) {
        cmd.execute();
      }
    }
    this.isOpen = true;
    while (this.isOpen) {
      if (InteractiveInputDevice.class.isInstance(getIn())) {
        ((InteractiveInputDevice) getIn()).write("> ");
      }
      while (!this.in.hasNext()) {
        this.in.closeIn();
        this.isOpen = false;
        return;
      }
      Optional<Command> cmd = processCommandLine();
      if (cmd.isEmpty()) {
        getOut()
            .writeln(
                "[ERROR] Команда не найдена: введите 'help' для просмотра списка доступных команд.");
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

  @Override
  public DataInputDevice getIn() {
    return (DataInputDevice) this.in;
  }

  @Override
  public TicketShell forkSubshell() {
    TicketShell subshell = new TicketShell(getIn(), getOut());
    for (Command cmd : this) {
      subshell.addCommand(cmd.setShell(subshell));
    }
    return subshell;
  }
}
