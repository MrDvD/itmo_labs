package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowLastCommand<T> implements Command<Optional<T>> {
  private final CachedCrudWorker<T, Set<T>, Long> collection;
  private final Comparator<T> comparator;

  public ShowLastCommand(CachedCrudWorker<T, Set<T>, Long> collection, Comparator<T> comparator) {
    this.collection = collection;
    this.comparator = comparator;
  }

  @Override
  public Optional<T> execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getAll().isEmpty()) {
      throw new RuntimeException("Коллекция пуста.");
    }
    List<T> sortedList =
        this.collection.getAll().stream().sorted((a, b) -> this.comparator.compare(a, b)).toList();
    return Optional.of(sortedList.get(sortedList.size() - 1));
  }

  @Override
  public String name() {
    return "show_last";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "показать последний элемент из коллекции";
  }
}
