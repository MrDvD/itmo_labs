package com.itmo.mrdvd.commands;

import java.util.List;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.AuthContext;
import com.itmo.mrdvd.service.executor.Command;

public class LoginCommand implements Command<Void> {
  private final AuthContext<LoginPasswordPair> authContext;

  public LoginCommand(AuthContext<LoginPasswordPair> authContext) {
    this.authContext = authContext;
  }

  @Override
  public Void execute(List<Object> params) {
    if (this.authContext == null) {
      throw new IllegalStateException("Не предоставлен контекст аутентификации.");
    }
    if (params.size() < 2) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    if (!String.class.isInstance(params.get(0)) || !String.class.isInstance(params.get(1))) {
      throw new IllegalArgumentException("Не удалось распознать аргументы команды.");
    }
    String login = (String) params.get(0);
    String password = (String) params.get(1);
    LoginPasswordPair pair = new LoginPasswordPair();
    pair.setLogin(login);
    pair.setPassword(password);
    this.authContext.setAuth(pair);
    return null;
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
    return "сохранить связку логин-пароль для будущих запросов к коллекции";
  }
}
