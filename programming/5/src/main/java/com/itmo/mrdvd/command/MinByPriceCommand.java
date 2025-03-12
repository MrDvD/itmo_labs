package com.itmo.mrdvd.command;

import java.util.Comparator;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;

public class MinByPriceCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;
  private final Comparator<Ticket> comparator;

  public MinByPriceCommand(TicketCollection collect, Comparator<Ticket> comparator, OutputDevice out) {
    this.collection = collect;
    this.out = out;
    this.comparator = comparator;
  }

  @Override
  public void execute(String[] params) {
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
