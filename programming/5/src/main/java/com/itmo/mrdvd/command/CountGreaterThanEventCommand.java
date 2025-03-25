package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.LongInputDevice;
import com.itmo.mrdvd.object.Ticket;
import java.util.Optional;

public class CountGreaterThanEventCommand implements CommandHasParams {
  private final Collection<Ticket, ?> collection;
  private final LongInputDevice in;
  private final OutputDevice out;

  public CountGreaterThanEventCommand(
      Collection<Ticket, ?> collect, LongInputDevice in, OutputDevice out) {
    this.collection = collect;
    this.in = in;
    this.out = out;
  }

  @Override
  public LongInputDevice getParamsInput() {
    return this.in;
  }

  @Override
  public void execute() {
    Optional<Long> params = getParamsInput().readLong();
    getParamsInput().skipLine();
    if (params.isEmpty()) {
      out.writeln("[ERROR] Неправильный формат ввода: event_id должен быть целым числом.");
      return;
    }
    Long eventId = params.get();
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
