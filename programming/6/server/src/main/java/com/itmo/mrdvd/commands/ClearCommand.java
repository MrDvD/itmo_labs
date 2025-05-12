package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class ClearCommand implements Command<Void> {
  private final CollectionWorker<?, ?> collection;

  public ClearCommand(CollectionWorker<?, ?> collect) {
    this.collection = collect;
  }

  @Override
  public Void execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для очистки.");
    }
    collection.clear();
    return null;
  }

  @Override
  public String name() {
    return "clear";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "очистить коллекцию";
  }
}
