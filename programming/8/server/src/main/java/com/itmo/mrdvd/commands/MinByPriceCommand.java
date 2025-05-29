package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MinByPriceCommand<T> implements Command<String> {
  private final CrudWorker<T, Set<T>, ?> collection;
  private final Comparator<T> comparator;

  public MinByPriceCommand(CrudWorker<T, Set<T>, ?> collect, Comparator<T> comparator) {
    this.collection = collect;
    this.comparator = comparator;
  }

  @Override
  public String execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getAll().isEmpty()) {
      throw new RuntimeException("Коллекция пуста.");
    }
    return this.collection.getAll().stream().min(this.comparator).toString();
  }

  @Override
  public String name() {
    return "min_by_price";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести любой объект из коллекции, значение поля price которого является минимальным";
  }
}
