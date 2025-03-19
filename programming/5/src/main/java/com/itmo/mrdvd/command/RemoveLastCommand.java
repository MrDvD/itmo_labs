package com.itmo.mrdvd.command;

import java.util.List;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.device.OutputDevice;

public class RemoveLastCommand<T> implements Command {
  private final CollectionWorker<T, List<T>> collection;
  private final OutputDevice out;

  public RemoveLastCommand(CollectionWorker<T, List<T>> collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute() {
   try {
      collection.getCollection().remove(collection.getCollection().size() - 1);   
   } catch (IndexOutOfBoundsException e) {
      out.writeln("[WARN] Коллекция пуста.");
   }
  }

  @Override
  public String name() {
    return "remove_last";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "удалить последний элемент из коллекции";
  }
}
