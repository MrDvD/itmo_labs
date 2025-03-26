package com.itmo.mrdvd.command;

import java.io.IOException;
import java.util.Optional;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.shell.Shell;

public class CountGreaterThanEventCommand implements Command {
  private final Collection<Ticket, ?> collection;
  private final Shell<?, ?> shell;

  public CountGreaterThanEventCommand(Collection<Ticket, ?> collect) {
    this(collect, null);
  }

  public CountGreaterThanEventCommand(
      Collection<Ticket, ?> collect, Shell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public CountGreaterThanEventCommand setShell(Shell<?, ?> shell) {
    return new CountGreaterThanEventCommand(collection, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    Optional<Long> params = Optional.empty();
    try {
      params = getShell().get().getIn().readLong();
      getShell().get().getIn().skipLine();
    } catch (IOException e) {}
    if (params.isEmpty()) {
      getShell().get().getOut().writeln("[ERROR] Неправильный формат ввода: event_id должен быть целым числом.");
      return;
    }
    Long eventId = params.get();
    int count = 0;
    for (Ticket ticket : collection) {
      if (ticket.getEvent().getId() > eventId) {
        count++;
      }
    }
    getShell().get().getOut().writeln(String.format("Количество элементов с большим event_id: %d.", count));
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

  @Override
  public boolean hasParams() {
    return true;
  }
}
