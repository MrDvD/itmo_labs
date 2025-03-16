package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;

public class RemoveLastCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;

  public RemoveLastCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute(String[] params) {
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
