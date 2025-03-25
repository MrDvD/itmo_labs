package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.device.OutputDevice;
import java.util.Comparator;
import java.util.List;

public class PrintFieldDescendingTypeCommand<T extends HavingId> implements Command {
  private final Collection<T, List<T>> collection;
  private final OutputDevice out;
  private final Comparator<T> comparator;

  public PrintFieldDescendingTypeCommand(
      Collection<T, List<T>> collect, Comparator<T> comparator, OutputDevice out) {
    this.collection = collect;
    this.out = out;
    this.comparator = comparator;
  }

  @Override
  public void execute() {
    collection.getCollection().sort(comparator);
    if (collection.getCollection().isEmpty()) {
      out.writeln("[INFO] Коллекция пуста.");
    } else {
      for (T obj : collection) {
        out.writeln(obj.toString());
      }
    }
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
