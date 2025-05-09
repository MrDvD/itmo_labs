package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;

public class MinByPriceCommand<T extends HavingId> implements Command<String> {
  private final CollectionWorker<T, List<T>> collection;
  private final Comparator<T> comparator;

  public MinByPriceCommand(CollectionWorker<T, List<T>> collect, Comparator<T> comparator) {
    this.collection = collect;
    this.comparator = comparator;
  }

  @Override
  public String execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    this.collection.getCollection().sort(comparator);
    return this.collection.getCollection().isEmpty()
        ? "Коллекция пуста."
        : collection.getCollection().get(0).toString();
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
