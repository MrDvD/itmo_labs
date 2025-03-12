package com.itmo.mrdvd.command;

import java.util.Comparator;
import java.util.Optional;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;

public class AddIfMaxCommand extends AddCommand {
   private final Comparator<Ticket> comparator;

  public AddIfMaxCommand(TicketCollection collect, Comparator<Ticket> comparator, InteractiveInputDevice in, OutputDevice out) {
    super(collect, in, out);
    this.comparator = comparator;
  }

  @Override
  public void execute(String[] params) {
    Ticket ticket = createTicketInteractive();
    collect.getCollection().sort(comparator);
    if (collect.getCollection().isEmpty() || collect.getCollection().get(collect.getCollection().size() - 1).getId().compareTo(ticket.getId()) < 0) {
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
