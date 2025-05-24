package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class RemoveByIdCommand implements Command<Void> {
  private final CachedCrudWorker<AuthoredTicket, ?, Long> collection;

  public RemoveByIdCommand(CachedCrudWorker<AuthoredTicket, ?, Long> collection) {
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws NullPointerException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    if (params.size() < 2) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    Long id = null;
    if (Long.class.isInstance(params.get(0))) {
      id = (Long) params.get(0);
    } else {
      try {
        id = Long.valueOf((String) params.get(0));
      } catch (NumberFormatException | ClassCastException e) {
        throw new IllegalArgumentException("Не удалось распознать id элемента.");
      }
    }
    if (!(params.get(1) instanceof LoginPasswordPair)) {
      throw new IllegalArgumentException("Не предоставлены реквизиты для работы.");
    }
    LoginPasswordPair pair = (LoginPasswordPair) params.get(1);
    if (id < 0) {
      throw new IllegalArgumentException("Параметр id не может быть отрицательным.");
    }
    this.collection
        .get(id)
        .ifPresentOrElse(
            (t) -> {
              if (!t.getAuthor().equals(pair.getLogin())) {
                throw new IllegalArgumentException("Не удалось удалить чужой элемент.");
              }
            },
            () -> {
              throw new IllegalArgumentException("Элемент с таким id не найден.");
            });
    this.collection.remove(id);
    return null;
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
