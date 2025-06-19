package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class ShowCommand implements Command<String> {
  private final Iterable<?> collection;

  public ShowCommand(Iterable<?> collect) {
    this.collection = collect;
  }

  @Override
  public String execute(List<Object> params) throws IllegalStateException {
    if (this.collection == null) {
      throw new IllegalStateException("Не предоставлена объект для чтения.");
    }
    String result = "";
    for (Object obj : collection) {
      result += obj.toString() + "\n";
    }
    return result + "Конец коллекции.";
  }

  @Override
  public String name() {
    return "show";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
  }
}
