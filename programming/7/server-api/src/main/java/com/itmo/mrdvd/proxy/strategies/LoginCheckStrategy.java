package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.List;
import java.util.Optional;

public class LoginCheckStrategy implements ProxyStrategy {
  private final Proxy loginProxy;
  private final String cmdLoginName;
  private final ProxyStrategy next;

  public LoginCheckStrategy(Proxy loginProxy, String cmdLoginName, ProxyStrategy next) {
    this.loginProxy = loginProxy;
    this.cmdLoginName = cmdLoginName;
    this.next = next;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    if (q.getArgs().size() < 2) {
      return Optional.of(
          ServiceQuery.of("login_error", List.of("Не удалось распознать реквизиты для запроса.")));
    }
    // making another query
    Optional<ServiceQuery> loginCheck =
        this.loginProxy.processQuery(ServiceQuery.of(this.cmdLoginName, q.getArgs()));
    if (loginCheck.isPresent()
        && !loginCheck.get().getArgs().isEmpty()
        && loginCheck.get().getArgs().get(0) instanceof Boolean success) {
      if (success) {
        return this.next.make(ServiceQuery.of(q.getName(), q.getArgs().stream().skip(2).toList()));
      }
      return Optional.of(
          ServiceQuery.of("login_error", List.of("Некорректные реквизиты для запроса.")));
    } else {
      return Optional.of(
          ServiceQuery.of("login_error", List.of("Не удалось проверить реквизиты для запроса.")));
    }
  }
}
