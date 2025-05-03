package com.itmo.mrdvd.public_scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketComparator;
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
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;

public class PublicServerExecutor extends AbstractExecutor {
  public PublicServerExecutor(Collection<Ticket, List<Ticket>> collect) {
    this(collect, new HashMap<>(), new HashMap<>());
  }

  public PublicServerExecutor(
      Collection<Ticket, List<Ticket>> collect, Map<String, Command<?>> commands, Map<String, Query> cachedQueries) {
    super(commands, cachedQueries);
    setCommand(new FetchAllCommand(this));
    // setCommand(new AddCommand<>(collect, builder));
    setCommand(new ClearCommand(collect));
    setCommand(new InfoCommand(collect));
    setCommand(new MinByPriceCommand(collect, new TicketComparator(TicketField.PRICE)));
    setCommand(new RemoveLastCommand<>(collect));
    setCommand(new ShowCommand(collect));
    setCommand(new PrintFieldDescendingTypeCommand(collect, new TicketComparator(TicketField.TYPE)));
    setCommand(new RemoveAtCommand<>(collect));
    setCommand(new RemoveByIdCommand(collect));
    setCommand(new CountGreaterThanEventCommand(collect));
  }
}
