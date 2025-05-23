package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CacheWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.function.Predicate;

public class UpdateCommand<T> implements Command<Void> {
  private final CacheWorker<T, ?, Long> collect;
  private final Predicate<T> cond;

  public UpdateCommand(CacheWorker<T, ?, Long> collection, Predicate<T> cond) {
    this.collect = collection;
    this.cond = cond;
  }

  @Override
  public Void execute(List<Object> params) {
    if (this.collect == null) {
      throw new IllegalStateException("Не задана коллекция для работы.");
    }
    if (params.size() < 2) {
      throw new IllegalArgumentException("Не предоставлен объект для обновления в коллекции.");
    }
    Long id = null;
    if ((params.get(0) instanceof Long)) {
      id = (Long) params.get(0);
    } else {
      try {
        id = Long.valueOf((String) params.get(0));
      } catch (NumberFormatException | ClassCastException e) {
        throw new IllegalArgumentException("Не удалось распознать id элемента.");
      }
    }
    if (this.collect.get(id).isEmpty()) {
      throw new IllegalArgumentException("В коллекции нет элемента с введённым id.");
    }
    try {
      T obj = (T) params.get(1);
      if (collect.update(id, obj, cond).isEmpty()) {
        throw new RuntimeException("Не удалось обновить элемент в коллекции.");
      }
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать переданный объект.");
    }
    return null;
  }

  @Override
  public String name() {
    return "update";
  }

  @Override
  public String signature() {
    return name() + " id {element}";
  }

  @Override
  public String description() {
    return "обновить значение элемента коллекции, id которого равен заданному";
  }
}
