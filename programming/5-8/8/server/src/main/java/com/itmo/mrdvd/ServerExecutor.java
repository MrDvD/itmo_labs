package com.itmo.mrdvd;

import com.itmo.mrdvd.collection.AccessWorker;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.collection.SelfContainedHash;
import com.itmo.mrdvd.collection.ticket.NodeComparator;
import com.itmo.mrdvd.collection.ticket.TicketComparator;
import com.itmo.mrdvd.commands.AddCommand;
import com.itmo.mrdvd.commands.CountGreaterThanEventCommand;
import com.itmo.mrdvd.commands.InfoCommand;
import com.itmo.mrdvd.commands.LoginCommand;
import com.itmo.mrdvd.commands.RegisterCommand;
import com.itmo.mrdvd.commands.RemoveByIdCommand;
import com.itmo.mrdvd.commands.ShowAtCommand;
import com.itmo.mrdvd.commands.ShowByIdCommand;
import com.itmo.mrdvd.commands.ShowCommand;
import com.itmo.mrdvd.commands.ShowLastCommand;
import com.itmo.mrdvd.commands.UpdateCommand;
import com.itmo.mrdvd.commands.ValidateLoginCommand;
import com.itmo.mrdvd.mappers.Mapper;
import com.itmo.mrdvd.object.TicketField;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import com.itmo.mrdvd.validators.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerExecutor extends AbstractExecutor {
  public ServerExecutor(
      CachedCrudWorker<Node, Set<Node>, Long> objectWorker,
      Validator<Node> validator,
      CachedCrudWorker<Credentials, Set<Credentials>, String> loginWorker,
      AccessWorker<Map<String, Object>> metaAccessor,
      Mapper<Credentials, String> tokenMapper,
      Validator<AuthID> idValidator,
      SelfContainedHash hash) {
    this(
        objectWorker,
        validator,
        loginWorker,
        metaAccessor,
        tokenMapper,
        idValidator,
        hash,
        new HashMap<>(),
        new HashMap<>());
  }

  public ServerExecutor(
      CachedCrudWorker<Node, Set<Node>, Long> objectWorker,
      Validator<Node> validator,
      CachedCrudWorker<Credentials, Set<Credentials>, String> loginWorker,
      AccessWorker<Map<String, Object>> metaAccessor,
      Mapper<Credentials, String> tokenMapper,
      Validator<AuthID> idValidator,
      SelfContainedHash hash,
      Map<String, Command<?>> commands,
      Map<String, CommandMeta> cache) {
    super(commands, cache);
    setCommand(new InfoCommand(metaAccessor));
    setCommand(
        new ShowLastCommand<>(
            objectWorker, new NodeComparator(new TicketComparator(TicketField.CREATION_DATE))));
    setCommand(new ShowCommand<>(objectWorker));
    setCommand(new ShowByIdCommand<>(objectWorker));
    setCommand(
        new ShowAtCommand<>(
            objectWorker, new NodeComparator(new TicketComparator(TicketField.CREATION_DATE))));
    setCommand(new RemoveByIdCommand(objectWorker));
    setCommand(new CountGreaterThanEventCommand(objectWorker));
    setCommand(new AddCommand<>(objectWorker, validator, Node.class));
    setCommand(new UpdateCommand<>(objectWorker, (t) -> validator.validate(t)));
    setCommand(new LoginCommand(loginWorker, hash, tokenMapper));
    setCommand(new RegisterCommand(loginWorker));
    setCommand(new ValidateLoginCommand(idValidator));
  }
}
