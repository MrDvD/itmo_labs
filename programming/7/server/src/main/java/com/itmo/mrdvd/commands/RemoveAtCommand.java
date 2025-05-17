package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class RemoveAtCommand<T extends HavingId> implements Command<Void> {
  private final CollectionWorker<T, List<T>> collection;

  public RemoveAtCommand(CollectionWorker<T, List<T>> collection) {
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getCollection().isEmpty()) {
      throw new RuntimeException("Коллекция пуста.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    Integer idx = null;
    if (Integer.class.isInstance(params.get(0))) {
      idx = (Integer) params.get(0);
    } else {
      try {
        idx = Integer.valueOf((String) params.get(0));
      } catch (NumberFormatException | ClassCastException e) {
        throw new IllegalArgumentException("Не удалось распознать индекс элемента.");
      }
    }
    if (idx < 0) {
      throw new IllegalArgumentException("Индекс элемента не может быть отрицательным.");
    }
    if (idx >= collection.getCollection().size()) {
      throw new IllegalArgumentException("В коллекции нет элемента с введённым индексом.");
    }
    this.collection.getCollection().remove(idx.intValue());
    return null;
  }

  @Override
  public String name() {
    return "remove_at";
  }

  @Override
  public String signature() {
    return name() + " index";
  }

  @Override
  public String description() {
    return "удалить элемент, находящийся в заданной позиции коллекции (index)";
  }
}
