package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class CountGreaterThanEventCommand implements Command<String> {
  private final CachedCrudWorker<Node, ?, Long> collection;

  public CountGreaterThanEventCommand(CachedCrudWorker<Node, ?, Long> collect) {
    this.collection = collect;
  }

  @Override
  public String execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    Long id = null;
    if (Long.class.isInstance(params.get(0))) {
      id = (Long) params.get(0);
    } else {
      try {
        id = Long.valueOf((String) params.get(0));
      } catch (NumberFormatException | ClassCastException e) {
        throw new IllegalArgumentException("Не удалось распознать id события.");
      }
    }
    if (id < 0) {
      throw new IllegalArgumentException("Параметр id не может быть отрицательным.");
    }
    int count = 0;
    for (Node node : collection) {
      if (node.getItem().getTicket().getEvent().getId() > id) {
        count++;
      }
    }
    return String.format("Количество элементов с большим event_id: %d.", count);
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
