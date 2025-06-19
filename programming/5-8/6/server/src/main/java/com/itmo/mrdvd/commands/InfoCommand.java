package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class InfoCommand implements Command<String> {
  private final Collection<?, ?> collection;

  public InfoCommand(Collection<?, ?> collect) {
    this.collection = collect;
  }

  @Override
  public String execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    return this.collection.getMetadata().toString();
  }

  @Override
  public String name() {
    return "info";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести в стандартный поток вывода информацию о коллекции";
  }
}
