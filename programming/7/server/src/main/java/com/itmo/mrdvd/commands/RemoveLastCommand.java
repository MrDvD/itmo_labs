package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CacheWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class RemoveLastCommand implements Command<Void> {
  private final CacheWorker<AuthoredTicket, Set<AuthoredTicket>, Long> collection;

  public RemoveLastCommand(CacheWorker<AuthoredTicket, Set<AuthoredTicket>, Long> collection) {
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
    if (params.isEmpty() || !(params.get(0) instanceof LoginPasswordPair)) {
      throw new IllegalArgumentException("Не предоставлены реквизиты для работы.");
    }
    LoginPasswordPair pair = (LoginPasswordPair) params.get(0);
    List<AuthoredTicket> sortedList =
        this.collection.getAll().stream()
            .sorted((a, b) -> a.getCreationDate().compareTo(b.getCreationDate()))
            .toList();
    AuthoredTicket toRemove = sortedList.get(sortedList.size() - 1);
    if (!toRemove.getAuthor().equals(pair.getLogin())) {
      throw new IllegalArgumentException("Не удалось удалить чужой элемент.");
    }
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
