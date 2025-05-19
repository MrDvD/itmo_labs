package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;

public class PrintFieldDescendingTypeCommand<T extends HavingId> implements Command<String> {
  private final Collection<T, List<T>> collection;
  private final Comparator<T> comparator;

  public PrintFieldDescendingTypeCommand(Collection<T, List<T>> collect, Comparator<T> comparator) {
    this.collection = collect;
    this.comparator = comparator;
  }

  @Override
  public String execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    collection.getAll().sort(comparator);
    String result = "";
    for (T obj : collection) {
      result += obj.toString() + "\n";
    }
    return result + "Конец коллекции.";
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
