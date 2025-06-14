package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowAtCommand<T> implements Command<Optional<T>> {
  private final CachedCrudWorker<T, Set<T>, Long> collection;
  private final Comparator<T> comparator;

  public ShowAtCommand(CachedCrudWorker<T, Set<T>, Long> collection, Comparator<T> comparator) {
    this.collection = collection;
    this.comparator = comparator;
  }

  @Override
  public Optional<T> execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getAll().isEmpty()) {
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
    if (idx >= collection.getAll().size()) {
      throw new IllegalArgumentException("В коллекции нет элемента с введённым индексом.");
    }
    List<T> sortedList =
        this.collection.getAll().stream().sorted((a, b) -> this.comparator.compare(a, b)).toList();
    return Optional.of(sortedList.get(idx));
  }

  @Override
  public String name() {
    return "show_at";
  }

  @Override
  public String signature() {
    return name() + " index";
  }

  @Override
  public String description() {
    return "показать элемент, находящийся в заданной позиции коллекции (index)";
  }
}
