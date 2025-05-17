package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class RemoveByIdCommand implements Command<Void> {
  private final CollectionWorker<?, ?> collection;

  public RemoveByIdCommand(CollectionWorker<?, ?> collection) {
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    Long id = null;
    if (Long.class.isInstance(params.get(0))) {
      id = (Long) params.get(0);
    } else {
      try {
        id = Long.valueOf((String) params.get(0));
      } catch (NumberFormatException | ClassCastException e) {
        throw new IllegalArgumentException("Не удалось распознать id элемента.");
      }
    }
    if (id < 0) {
      throw new IllegalArgumentException("Параметр id не может быть отрицательным.");
    }
    this.collection.remove(id);
    return null;
  }

  @Override
  public String name() {
    return "remove_by_id";
  }

  @Override
  public String signature() {
    return name() + " id";
  }

  @Override
  public String description() {
    return "удалить элемент из коллекции по его id";
  }
}
