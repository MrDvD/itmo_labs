package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class PrintFieldDescendingTypeCommand<T> implements Command<String> {
  private final CrudWorker<T, Set<T>, ?> collection;
  private final Comparator<T> comparator;

  public PrintFieldDescendingTypeCommand(
      CrudWorker<T, Set<T>, ?> collect, Comparator<T> comparator) {
    this.collection = collect;
    this.comparator = comparator;
  }

  @Override
  public String execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    StringBuilder result = new StringBuilder();
    this.collection.getAll().stream()
        .sorted(this.comparator)
        .forEach(obj -> result.append(obj.toString()).append("\n"));
    return result.toString() + "Конец коллекции.";
  }

  @Override
  public String name() {
    return "print_field_descending_type";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести значения поля type всех элементов в порядке убывания";
  }
}
