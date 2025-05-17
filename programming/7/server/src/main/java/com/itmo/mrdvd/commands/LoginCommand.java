package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class LoginCommand implements Command<Boolean> {
  public LoginCommand() {}

  @Override
  public Boolean execute(List<Object> params) throws IllegalStateException {
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Не предоставлен объект для добавления в коллекцию.");
    }
    if (params.size() < 2) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    String login = null, password = null;
    try {
      login = (String) params.get(0);
      password = (String) params.get(1);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать аргументы команды.");
    }
    // here comes magical interaction with database
    return false;
  }

  @Override
  public String name() {
    return "login";
  }

  @Override
  public String signature() {
    return name() + " {login} {password}";
  }

  @Override
  public String description() {
    return "проверить реквизиты для входа";
  }
}
