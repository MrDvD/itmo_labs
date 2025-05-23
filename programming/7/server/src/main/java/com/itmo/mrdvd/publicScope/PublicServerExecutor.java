package com.itmo.mrdvd.publicScope;

import com.itmo.mrdvd.collection.CacheWorker;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.login.SelfContainedHash;
import com.itmo.mrdvd.collection.ticket.TicketComparator;
import com.itmo.mrdvd.commands.AddCommand;
import com.itmo.mrdvd.commands.AddIfCommand;
import com.itmo.mrdvd.commands.ClearCommand;
import com.itmo.mrdvd.commands.CountGreaterThanEventCommand;
import com.itmo.mrdvd.commands.FetchAllCommand;
import com.itmo.mrdvd.commands.InfoCommand;
import com.itmo.mrdvd.commands.LoginCommand;
import com.itmo.mrdvd.commands.MinByPriceCommand;
import com.itmo.mrdvd.commands.PrintFieldDescendingTypeCommand;
import com.itmo.mrdvd.commands.RegisterCommand;
import com.itmo.mrdvd.commands.RemoveAtCommand;
import com.itmo.mrdvd.commands.RemoveByIdCommand;
import com.itmo.mrdvd.commands.RemoveLastCommand;
import com.itmo.mrdvd.commands.ShowCommand;
import com.itmo.mrdvd.commands.UpdateCommand;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import com.itmo.mrdvd.validators.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PublicServerExecutor extends AbstractExecutor {
  public PublicServerExecutor(
      Collection<AuthoredTicket, Set<AuthoredTicket>> collect,
      Validator<? super Ticket> validator,
      CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker,
      SelfContainedHash hash) {
    this(collect, validator, loginWorker, hash, new HashMap<>(), new HashMap<>());
  }

  public PublicServerExecutor(
      Collection<AuthoredTicket, Set<AuthoredTicket>> collect,
      Validator<? super Ticket> validator,
      CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker,
      SelfContainedHash hash,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cache) {
    super(commands, cache);
    setCommand(new FetchAllCommand(this));
    setCommand(new ClearCommand(collect));
    setCommand(new InfoCommand(collect));
    setCommand(new MinByPriceCommand(collect, new TicketComparator(TicketField.PRICE)));
    setCommand(new RemoveLastCommand(collect));
    setCommand(new ShowCommand(collect));
    setCommand(
        new PrintFieldDescendingTypeCommand(collect, new TicketComparator(TicketField.TYPE)));
    setCommand(
        new RemoveAtCommand(
            collect,
            (t) -> {
              Optional<AuthoredTicket> old = collect.get(t.getId());
              return old.isPresent() && old.get().getAuthor().equals(t.getAuthor());
            }));
    setCommand(new RemoveByIdCommand(collect));
    setCommand(new CountGreaterThanEventCommand(collect));
    setCommand(new AddCommand(collect, validator, AuthoredTicket.class));
    setCommand(
        new AddIfCommand(
            collect,
            validator,
            new TicketComparator(TicketField.ID),
            AuthoredTicket.class,
            Set.of(1)));
    setCommand(
        new UpdateCommand<>(
            collect,
            (t) -> {
              if (validator.validate(t)) {
                Optional<AuthoredTicket> old = collect.get(t.getId());
                return old.isPresent() && old.get().getAuthor().equals(t.getAuthor());
              }
              return false;
            }));
    setCommand(new LoginCommand(loginWorker, hash));
    setCommand(new RegisterCommand(loginWorker));
  }
}
