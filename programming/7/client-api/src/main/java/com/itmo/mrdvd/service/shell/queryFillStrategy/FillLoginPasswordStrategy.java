package com.itmo.mrdvd.service.shell.queryFillStrategy;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.io.IOException;
import java.util.stream.Stream;

public class FillLoginPasswordStrategy implements QueryFillStrategy {
  private final QueryFillStrategy prev;

  public FillLoginPasswordStrategy() {
    this(null);
  }

  public FillLoginPasswordStrategy(QueryFillStrategy prev) {
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    if (q.getArgs().size() < 2
        || !String.class.isInstance(q.getArgs().get(0))
        || !String.class.isInstance(q.getArgs().get(1))) {
      throw new IllegalArgumentException("Не предоставлены аргументы для заполнения реквизитов.");
    }
    LoginPasswordPair pair = new LoginPasswordPair();
    pair.setLogin((String) q.getArgs().get(0));
    pair.setPassword((String) q.getArgs().get(1));
    return ServiceQuery.of(
        q.getName(), Stream.concat(Stream.of(pair), q.getArgs().stream().skip(2)).toList());
  }
}
