package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket.TicketParser;
import com.itmo.mrdvd.object.Ticket.TicketValidator;

public class RemoveByIdCommand implements Command {
  private TicketCollection collection;
  private OutputDevice out;

  public RemoveByIdCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  public int validateParams(String[] params) {
    if (params.length != 1) {
      return -3;
    }
    Long id = TicketParser.parseId(params[0]);
    if (!TicketValidator.validateId(id)) {
      return -1;
    }
    if (!collection.getTicketIdGenerator().isTaken(id)) {
      return -2;
    }
    return 0;
  }

  @Override
  public void execute(String[] params) {
    int validationResult = validateParams(params);
    if (validationResult != 0) {
      switch (validationResult) {
        case -1 -> out.writeln("[ERROR] Неправильный формат ввода: id должен быть целым числом.");
        case -2 -> out.writeln("[ERROR] Указанный id не найден в коллекции.");
        default -> out.writeln("[ERROR] Неправильный формат ввода параметров команды.");
      }
      return;
    }
    collection.remove(TicketParser.parseId(params[0]));
  }

  @Override
  public String name() {
    return "remove_by_id";
  }

  @Override
  public String signature() {
    return name() + " id";
  }

  @Override
  public String description() {
    return "удалить элемент из коллекции по его id";
  }
}
