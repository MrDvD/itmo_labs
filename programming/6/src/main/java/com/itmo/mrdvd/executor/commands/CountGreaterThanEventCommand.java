package com.itmo.mrdvd.executor.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.shell.DefaultShell;
import java.io.IOException;
import java.util.Optional;

public class CountGreaterThanEventCommand implements Command {
  private final Collection<Ticket, ?> collection;
  private final DefaultShell<?, ?> shell;

  public CountGreaterThanEventCommand(Collection<Ticket, ?> collect) {
    this(collect, null);
  }

  public CountGreaterThanEventCommand(Collection<Ticket, ?> collect, DefaultShell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public CountGreaterThanEventCommand setShell(DefaultShell<?, ?> shell) {
    return new CountGreaterThanEventCommand(collection, shell);
  }

  @Override
  public Optional<DefaultShell<?, ?>> getShell() {
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
    } catch (IOException e) {
    }
    if (params.isEmpty()) {
      getShell()
          .get()
          .getOut()
          .writeln("[ERROR] Неправильный формат ввода: event_id должен быть целым числом.");
      return;
    }
    Long eventId = params.get();
    int count = 0;
    for (Ticket ticket : collection) {
      if (ticket.getEvent().getId() > eventId) {
        count++;
      }
    }
    getShell()
        .get()
        .getOut()
        .writeln(String.format("Количество элементов с большим event_id: %d.", count));
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
