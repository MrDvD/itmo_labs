package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CacheWorker;
import com.itmo.mrdvd.collection.login.SelfContainedHash;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LoginCommand implements Command<Boolean> {
  private final CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker;
  private final SelfContainedHash hash;

  public LoginCommand(
      CacheWorker<LoginPasswordPair, Set<LoginPasswordPair>, String> loginWorker,
      SelfContainedHash hash) {
    this.loginWorker = loginWorker;
    this.hash = hash;
  }

  @Override
  public Boolean execute(List<Object> params) throws IllegalStateException {
    if (this.loginWorker == null) {
      throw new IllegalStateException("Не предоставлен обработчик логина.");
    }
    if (this.hash == null) {
      throw new IllegalStateException("Не предоставлен обработчик хеширования.");
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
    Optional<? extends LoginPasswordPair> dbPair = this.loginWorker.get(pair.getLogin());
    if (dbPair.isPresent() && this.hash.compare(pair.getPassword(), dbPair.get().getPassword())) {
      return true;
    }
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
