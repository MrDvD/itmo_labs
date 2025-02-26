package com.itmo.mrdvd.command;

import java.util.ArrayList;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class MinByPriceCommand implements Command {
  private TicketCollection collection;
  private OutputDevice out;

  public MinByPriceCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute(String[] params) {
    ArrayList<Ticket> sorted = collection.sort(TicketField.PRICE);
    if (sorted.isEmpty()) {
      out.writeln("[INFO] Коллекция пуста.");
    } else {
      out.writeln(sorted.get(0).toString());
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
