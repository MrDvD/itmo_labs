package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.Credentials;
import com.itmo.mrdvd.collection.CachedCrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Set;

public class RegisterCommand implements Command<Void> {
  private final CachedCrudWorker<Credentials, Set<Credentials>, String> loginWorker;

  public RegisterCommand(CachedCrudWorker<Credentials, Set<Credentials>, String> loginWorker) {
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
    Credentials pair = null;
    try {
      pair = (Credentials) params.get(0);
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
