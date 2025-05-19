package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class RemoveLastCommand<T extends HavingId> implements Command<Void> {
  private final CollectionWorker<T, List<T>> collection;

  public RemoveLastCommand(CollectionWorker<T, List<T>> collection) {
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getAll().isEmpty()) {
      throw new RuntimeException("Коллекция пуста.");
    }
    collection.getAll().remove(collection.getAll().size() - 1);
    return null;
  }

  @Override
  public String name() {
    return "remove_last";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "удалить последний элемент из коллекции";
  }
}
