package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Set;

public class RemoveLastCommand implements Command<Void> {
  private final CrudWorker<AuthoredTicket, Set<AuthoredTicket>> collection;

  public RemoveLastCommand(CrudWorker<AuthoredTicket, Set<AuthoredTicket>> collection) {
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (this.collection.getAll().isEmpty()) {
      throw new RuntimeException("Коллекция пуста.");
    }
    List<AuthoredTicket> sortedList =
        this.collection.getAll().stream()
            .sorted((a, b) -> a.getCreationDate().compareTo(b.getCreationDate()))
            .toList();
    AuthoredTicket toRemove = sortedList.get(sortedList.size() - 1);
    this.collection.remove(toRemove.getId());
    return null;
  }

  @Override
  public String name() {
    return "remove_last";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "удалить последний элемент из коллекции";
  }
}
