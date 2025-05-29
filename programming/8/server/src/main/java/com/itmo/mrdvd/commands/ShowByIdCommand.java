package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowByIdCommand<T> implements Command<Optional<T>> {
  private final CachedCrudWorker<T, Set<T>, Long> collection;

  public ShowByIdCommand(CachedCrudWorker<T, Set<T>, Long> collect) {
    this.collection = collect;
  }

  @Override
  public Optional<T> execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена объект для чтения.");
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
    return this.collection.get(id);
  }

  @Override
  public String name() {
    return "show_by_id";
  }

  @Override
  public String signature() {
    return name() + " {id}";
  }

  @Override
  public String description() {
    return "вывести элемент коллекции по его id";
  }
}
