package com.itmo.mrdvd.proxy.strategies;

import java.util.Optional;
import java.util.stream.Stream;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.AuthContext;

public class FillAuthContextStrategy implements ProxyStrategy {
  private final AuthContext<?> authContext;

  public FillAuthContextStrategy(AuthContext<?> authContext) {
    this.authContext = authContext;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    if (this.authContext == null || this.authContext.getAuth().isEmpty()) {
      throw new IllegalStateException("Не предоставлен контекст аутентификации.");
    }
    Stream<Object> args = Stream.of(this.authContext.getAuth().get());
    if (q.getArgs() != null) {
      args = Stream.concat(args, q.getArgs().stream());
    }
    return Optional.of(ServiceQuery.of(q.getName(), args.toList()));
  }
}
