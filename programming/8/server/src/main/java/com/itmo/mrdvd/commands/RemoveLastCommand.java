package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.Ticket;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.executor.Command;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class RemoveLastCommand implements Command<Void> {
  private final CachedCrudWorker<Node, Set<Node>, Long> collection;
  private final Comparator<Ticket> comparator;

  public RemoveLastCommand(
      CachedCrudWorker<Node, Set<Node>, Long> collection, Comparator<Ticket> comparator) {
    this.collection = collection;
    this.comparator = comparator;
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
    List<Node> sortedList =
        this.collection.getAll().stream()
            .sorted(
                (a, b) -> this.comparator.compare(a.getItem().getTicket(), b.getItem().getTicket()))
            .toList();
    Node toRemove = sortedList.get(sortedList.size() - 1);
    if (!toRemove.getAuthor().equals(pair.getLogin())) {
      throw new IllegalArgumentException("Не удалось удалить чужой элемент.");
    }
    this.collection.remove(toRemove.getItem().getTicket().getId());
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
