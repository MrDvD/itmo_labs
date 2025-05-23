package com.itmo.mrdvd.publicScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketComparator;
import com.itmo.mrdvd.commands.AddCommand;
import com.itmo.mrdvd.commands.AddIfCommand;
import com.itmo.mrdvd.commands.ClearCommand;
import com.itmo.mrdvd.commands.CountGreaterThanEventCommand;
import com.itmo.mrdvd.commands.FetchAllCommand;
import com.itmo.mrdvd.commands.InfoCommand;
import com.itmo.mrdvd.commands.MinByPriceCommand;
import com.itmo.mrdvd.commands.PrintFieldDescendingTypeCommand;
import com.itmo.mrdvd.commands.RemoveAtCommand;
import com.itmo.mrdvd.commands.RemoveByIdCommand;
import com.itmo.mrdvd.commands.RemoveLastCommand;
import com.itmo.mrdvd.commands.ShowCommand;
import com.itmo.mrdvd.commands.UpdateCommand;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import com.itmo.mrdvd.validators.Validator;

public class PublicServerExecutor extends AbstractExecutor {
  public PublicServerExecutor(
      Collection<Ticket, List<Ticket>> collect, Validator<Ticket> validator) {
    this(collect, validator, new HashMap<>(), new HashMap<>());
  }

  public PublicServerExecutor(
      Collection<Ticket, List<Ticket>> collect,
      Validator<Ticket> validator,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cache) {
    super(commands, cache);
    setCommand(new FetchAllCommand(this));
    setCommand(new ClearCommand(collect));
    setCommand(new InfoCommand(collect));
    setCommand(new MinByPriceCommand(collect, new TicketComparator(TicketField.PRICE)));
    setCommand(new RemoveLastCommand<>(collect));
    setCommand(new ShowCommand(collect));
    setCommand(
        new PrintFieldDescendingTypeCommand(collect, new TicketComparator(TicketField.TYPE)));
    setCommand(new RemoveAtCommand<>(collect));
    setCommand(new RemoveByIdCommand(collect));
    setCommand(new CountGreaterThanEventCommand(collect));
    setCommand(new AddCommand<>(collect, validator, Ticket.class));
    setCommand(
        new AddIfCommand<>(
            collect, validator, new TicketComparator(TicketField.ID), Ticket.class, Set.of(1)));
    setCommand(new UpdateCommand<>(collect, validator));
  }
}
