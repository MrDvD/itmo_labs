package com.itmo.mrdvd.shell;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class TicketShell extends CollectionShell<Ticket> {
  public TicketShell(DataInputDevice in, OutputDevice out) {
    super(in, out);
  }

  public TicketShell(
      DataInputDevice in,
      OutputDevice out,
      Map<String, Command> commands,
      List<Command> preExecute) {
    super(in, out, commands, preExecute);
  }

  public void initDefaultCommands(
      Collection<Ticket, List<Ticket>> collection,
      String envName,
      DataFileDescriptor fd,
      Serializer<Collection<Ticket, List<Ticket>>> serial,
      Deserializer<Collection<Ticket, List<Ticket>>> deserial,
      Set<Path> usedPaths) {
    addCommand(
        new AddCommand<>(
            collection,
            new InteractiveTicketBuilder(
                new InteractiveCoordinatesBuilder(getIn(), getOut()),
                new InteractiveEventBuilder(getIn(), getOut()),
                getIn(),
                getOut())));
    addCommand(new HelpCommand());
    addCommand(new ExitCommand());
    addCommand(
        new UpdateCommand<>(
            collection,
            new InteractiveTicketUpdater(
                new InteractiveCoordinatesUpdater(getIn(), getOut()),
                new InteractiveEventUpdater(getIn(), getOut()),
                getIn(),
                getOut())));
    addCommand(new ClearCommand(collection));
    addCommand(new RemoveByIdCommand(collection));
    addCommand(new RemoveAtCommand<>(collection));
    addCommand(new RemoveLastCommand<>(collection));
    addCommand(new ShowCommand(collection));
    addCommand(
        new AddIfCommand<>(
            collection,
            new InteractiveTicketBuilder(
                new InteractiveCoordinatesBuilder(getIn(), getOut()),
                new InteractiveEventBuilder(getIn(), getOut()),
                getIn(),
                getOut()),
            new TicketComparator(TicketField.ID),
            Set.of(1)));
    addCommand(new MinByPriceCommand<>(collection, new TicketComparator(TicketField.PRICE)));
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
}
