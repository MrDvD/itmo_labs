package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.executor.CommandMeta;
import com.itmo.mrdvd.service.executor.DefaultCommandMeta;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FetchAllCommand implements Command<List<CommandMeta>> {
  private final AbstractExecutor exec;

  public FetchAllCommand(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public List<CommandMeta> execute(List<Object> params) {
    Stream<CommandMeta> queries = Stream.empty();
    for (String cmdName : this.exec.getCommandKeys()) {
      Optional<Command<?>> cmd = this.exec.getCommand(cmdName);
      if (cmd.isPresent()) {
        CommandMeta q = new DefaultCommandMeta();
        q.setCmd(cmd.get().name());
        q.setSignature(cmd.get().signature());
        q.setDesc(cmd.get().description());
        queries = Stream.concat(queries, Stream.of(q));
      }
    }
    return queries.toList();
  }

  @Override
  public String name() {
    return "fetch_all";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "получить от сервера доступные запросы";
  }
}
