package com.itmo.mrdvd.command;

import java.util.Comparator;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;

public class PrintFieldDescendingTypeCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;
  private final Comparator<Ticket> comparator;

  public PrintFieldDescendingTypeCommand(TicketCollection collect, Comparator<Ticket> comparator, OutputDevice out) {
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
      for (Ticket ticket : collection) {
        out.writeln(ticket.toString());
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
