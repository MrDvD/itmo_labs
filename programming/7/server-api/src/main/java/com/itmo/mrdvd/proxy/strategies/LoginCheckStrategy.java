package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class LoginCheckStrategy implements ProxyStrategy {
  private final Proxy loginProxy;
  private final String cmdLoginName;
  private final ProxyStrategy prev;

  public LoginCheckStrategy(Proxy loginProxy, String cmdLoginName) {
    this(loginProxy, cmdLoginName, null);
  }

  public LoginCheckStrategy(Proxy loginProxy, String cmdLoginName, ProxyStrategy prev) {
    this.loginProxy = loginProxy;
    this.cmdLoginName = cmdLoginName;
    this.prev = prev;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    if (this.prev != null) {
      Optional<ServiceQuery> newQ = this.prev.make(q);
      if (newQ.isPresent() && newQ.get().getName().equals(q.getName())) {
        q = newQ.get();
      } else {
        return newQ;
      }
    }
    if (q.getArgs().isEmpty()) {
      return Optional.of(
          ServiceQuery.of("login_error", List.of("Отсутствуют реквизиты у запроса.")));
    }
    // making another query
    Optional<ServiceQuery> loginCheck =
        this.loginProxy.processQuery(ServiceQuery.of(this.cmdLoginName, q.getArgs()));
    if (loginCheck.isPresent()
        && !loginCheck.get().getArgs().isEmpty()
        && loginCheck.get().getArgs().get(0) instanceof Boolean success) {
      if (success) {
        return Optional.of(
            ServiceQuery.of(
                q.getName(),
                Stream.concat(q.getArgs().stream().skip(1), Stream.of(q.getArgs().get(0)))
                    .toList()));
      }
      return Optional.of(
          ServiceQuery.of("login_error", List.of("Некорректные реквизиты для запроса.")));
    } else {
      return Optional.of(
          ServiceQuery.of("login_error", List.of("Не удалось проверить реквизиты для запроса.")));
    }
  }
}
