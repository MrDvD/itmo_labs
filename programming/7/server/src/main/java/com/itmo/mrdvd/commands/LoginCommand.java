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
    if (!String.class.isInstance(params.get(0)) || !String.class.isInstance(params.get(1))) {
      throw new IllegalArgumentException("Не удалось распознать аргументы команды.");
    }
    String login = (String) params.get(0);
    String password = (String) params.get(1);
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
