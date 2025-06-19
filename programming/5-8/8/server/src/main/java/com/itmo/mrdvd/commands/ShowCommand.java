package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Set;

public class ShowCommand<T> implements Command<Set<T>> {
  private final CachedCrudWorker<T, Set<T>, ?> collection;

  public ShowCommand(CachedCrudWorker<T, Set<T>, ?> collect) {
    this.collection = collect;
  }

  @Override
  public Set<T> execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена объект для чтения.");
    }
    return this.collection.getAll();
  }

  @Override
  public String name() {
    return "show";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести все элементы коллекции";
  }
}
