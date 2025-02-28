package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;
import java.util.ArrayList;

public class PrintFieldDescendingTypeCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;

  public PrintFieldDescendingTypeCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute(String[] params) {
    ArrayList<Ticket> sorted = collection.sort(TicketField.TYPE, true);
    if (sorted.isEmpty()) {
      out.writeln("[INFO] Коллекция пуста.");
    } else {
      for (Ticket ticket : sorted) {
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
