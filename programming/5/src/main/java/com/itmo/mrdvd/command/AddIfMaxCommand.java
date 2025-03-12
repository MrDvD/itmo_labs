package com.itmo.mrdvd.command;

import java.util.ArrayList;
import java.util.Optional;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class AddIfMaxCommand extends AddCommand {
  public AddIfMaxCommand(TicketCollection collect, InteractiveInputDevice in, OutputDevice out) {
    super(collect, in, out);
  }

  @Override
  public void execute(String[] params) {
    Ticket ticket = createTicketInteractive();
    ArrayList<Ticket> sorted = collect.sort(TicketField.ID);
    if (sorted.isEmpty() || sorted.get(sorted.size() - 1).getId().compareTo(ticket.getId()) < 0) {
      Optional<Ticket> result = collect.add(ticket);
      if (result.isPresent()) {
        out.writeln("[INFO] Билет успешно добавлен в коллекцию.");
      } else {
        out.writeln("[ERROR] Не удалось добавить билет в коллекцию.");
      }
    } else {
      out.writeln("[INFO] Билет не был добавлен в коллекцию: он не максимальный.");
    }
  }

  @Override
  public String name() {
    return "add_if_max";
  }

  @Override
  public String signature() {
    return name() + " {element}";
  }

  @Override
  public String description() {
    return "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
  }
}
