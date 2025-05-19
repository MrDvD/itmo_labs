package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Set;

public class RemoveAtCommand implements Command<Void> {
  private final CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> collection;

  public RemoveAtCommand(CrudWorker<AuthoredTicket, Set<AuthoredTicket>, Long> collection) {
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getAll().isEmpty()) {
      throw new RuntimeException("Коллекция пуста.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    Integer idx = null;
    if (Integer.class.isInstance(params.get(0))) {
      idx = (Integer) params.get(0);
    } else {
      try {
        idx = Integer.valueOf((String) params.get(0));
      } catch (NumberFormatException | ClassCastException e) {
        throw new IllegalArgumentException("Не удалось распознать индекс элемента.");
      }
    }
    if (idx < 0) {
      throw new IllegalArgumentException("Индекс элемента не может быть отрицательным.");
    }
    if (idx >= collection.getAll().size()) {
      throw new IllegalArgumentException("В коллекции нет элемента с введённым индексом.");
    }
    List<AuthoredTicket> sortedList =
        this.collection.getAll().stream()
            .sorted((a, b) -> a.getCreationDate().compareTo(b.getCreationDate()))
            .toList();
    AuthoredTicket toRemove = sortedList.get(idx);
    this.collection.remove(toRemove.getId());
    return null;
  }

  @Override
  public String name() {
    return "remove_at";
  }

  @Override
  public String signature() {
    return name() + " index";
  }

  @Override
  public String description() {
    return "удалить элемент, находящийся в заданной позиции коллекции (index)";
  }
}
