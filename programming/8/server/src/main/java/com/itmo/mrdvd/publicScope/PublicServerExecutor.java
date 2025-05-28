package com.itmo.mrdvd.publicScope;

import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.collection.AccessWorker;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.login.SelfContainedHash;
import com.itmo.mrdvd.collection.ticket.TicketComparator;
import com.itmo.mrdvd.commands.AddCommand;
import com.itmo.mrdvd.commands.AddIfCommand;
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
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.object.TicketField;
import com.itmo.mrdvd.proxy.mappers.Mapper;
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
      CachedCrudWorker<Node, Set<Node>, Long> objectWorker,
      Validator<Node> validator,
      CachedCrudWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker,
      AccessWorker<Map<String, Object>> metaAccessor,
      Mapper<? super Map<String, Object>, String> serializer,
      SelfContainedHash hash) {
    this(
        objectWorker,
        validator,
        loginWorker,
        metaAccessor,
        serializer,
        hash,
        new HashMap<>(),
        new HashMap<>());
  }

  public PublicServerExecutor(
      CachedCrudWorker<Node, Set<Node>, Long> objectWorker,
      Validator<Node> validator,
      CachedCrudWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker,
      AccessWorker<Map<String, Object>> metaAccessor,
      Mapper<? super Map<String, Object>, String> serializer,
      SelfContainedHash hash,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cache) {
    super(commands, cache);
    setCommand(new FetchAllCommand(this));
    setCommand(new InfoCommand(metaAccessor, serializer));
    setCommand(new MinByPriceCommand(objectWorker, new TicketComparator(TicketField.PRICE)));
    setCommand(
        new RemoveLastCommand(objectWorker, new TicketComparator(TicketField.CREATION_DATE)));
    setCommand(new ShowCommand<>(objectWorker));
    setCommand(
        new PrintFieldDescendingTypeCommand(objectWorker, new TicketComparator(TicketField.TYPE)));
    setCommand(
        new RemoveAtCommand(
            objectWorker,
            new TicketComparator(TicketField.CREATION_DATE),
            (t) -> {
              Optional<Node> old = objectWorker.get(t.getItem().getTicket().getId());
              return old.isPresent() && old.get().getAuthor().equals(t.getAuthor());
            }));
    setCommand(new RemoveByIdCommand(objectWorker));
    setCommand(new CountGreaterThanEventCommand(objectWorker));
    setCommand(new AddCommand<>(objectWorker, validator, Node.class));
    setCommand(
        new AddIfCommand(
            objectWorker, validator, new TicketComparator(TicketField.ID), Node.class, Set.of(1)));
    setCommand(
        new UpdateCommand<>(
            objectWorker,
            (t) -> {
              if (validator.validate(t)) {
                Optional<Node> old = objectWorker.get(t.getItem().getTicket().getId());
                return old.isPresent() && old.get().getAuthor().equals(t.getAuthor());
              }
              return false;
            }));
    setCommand(new LoginCommand(loginWorker, hash));
    setCommand(new RegisterCommand(loginWorker));
  }
}
