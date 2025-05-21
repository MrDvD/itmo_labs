package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CacheWorker;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Set;

public class RegisterCommand implements Command<Void> {
  private final CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker;

  public RegisterCommand(
      CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker) {
    this.loginWorker = loginWorker;
  }

  @Override
  public Void execute(List<Object> params) {
    if (this.loginWorker == null) {
      throw new IllegalStateException("Не предоставлен обработчик логина.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    LoginPasswordPair pair = null;
    try {
      pair = (LoginPasswordPair) params.get(0);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать аргументы команды.");
    }
    this.loginWorker.add(pair, p -> true);
    return null;
  }

  @Override
  public String name() {
    return "register";
  }

  @Override
  public String signature() {
    return name() + " {login} {password}";
  }

  @Override
  public String description() {
    return "зарегистрировать нового пользователя";
  }
}
