package com.itmo.mrdvd.command;

import java.util.Comparator;
import java.util.List;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.device.OutputDevice;

public class MinByPriceCommand<T extends HavingId> implements Command {
  private final CollectionWorker<T, List<T>> collection;
  private final OutputDevice out;
  private final Comparator<T> comparator;

  public MinByPriceCommand(CollectionWorker<T, List<T>> collect, Comparator<T> comparator, OutputDevice out) {
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
      out.writeln(collection.getCollection().get(0).toString());
    }
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
