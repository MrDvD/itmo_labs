package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Event.EventParser;
import com.itmo.mrdvd.object.Ticket;

public class CountGreaterThanEventCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;

  public CountGreaterThanEventCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  public int validateParams(String[] params) {
    if (params.length != 1) {
      return -3;
    }
    Long id = EventParser.parseId(params[0]);
    if (!Event.EventValidator.validateId(id)) {
      return -1;
    }
    return 0;
  }

  @Override
  public void execute(String[] params) {
    int validationResult = validateParams(params);
    if (validationResult != 0) {
      switch (validationResult) {
        case -1 ->
            out.writeln("[ERROR] Неправильный формат ввода: event_id должен быть целым числом.");
        default -> out.writeln("[ERROR] Неправильный формат ввода параметров команды.");
      }
      return;
    }
    Long eventId = EventParser.parseId(params[0]);
    int count = 0;
    for (Ticket ticket : collection) {
      if (ticket.getEvent().getId() > eventId) {
        count++;
      }
    }
    out.writeln(String.format("Количество элементов с большим event_id: %d.", count));
  }

  @Override
  public String name() {
    return "count_greater_than_event";
  }

  @Override
  public String signature() {
    return name() + " event_id";
  }

  @Override
  public String description() {
    return "вывести количество элементов, значение поля event_id которых больше заданного";
  }
}
