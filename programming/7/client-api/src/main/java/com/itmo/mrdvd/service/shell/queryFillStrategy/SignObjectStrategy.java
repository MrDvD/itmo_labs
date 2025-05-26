package com.itmo.mrdvd.service.shell.queryFillStrategy;

import com.itmo.mrdvd.object.AuthoredTicket;
import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.AuthContext;
import java.io.IOException;
import java.util.stream.Stream;

public class SignObjectStrategy implements QueryFillStrategy {
  private final AuthContext<LoginPasswordPair> authContext;
  private final int offset;
  private final QueryFillStrategy prev;

  public SignObjectStrategy(AuthContext<LoginPasswordPair> authContext, int offset) {
    this(authContext, offset, null);
  }

  public SignObjectStrategy(
      AuthContext<LoginPasswordPair> authContext, int offset, QueryFillStrategy prev) {
    this.authContext = authContext;
    this.offset = offset;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = this.prev.fillArgs(q);
    }
    if (this.authContext == null || this.authContext.getAuth().isEmpty()) {
      throw new IllegalStateException("Не предоставлен контекст авторизации.");
    }
    if (q.getArgs().isEmpty()) {
      throw new IllegalArgumentException("Не предоставлены аргументы для заполнения реквизитов.");
    }
    if (q.getArgs().get(offset) instanceof AuthoredTicket) {
      AuthoredTicket ticket = (AuthoredTicket) q.getArgs().get(offset);
      ticket.setAuthor(this.authContext.getAuth().get().getLogin());
      return ServiceQuery.of(
          q.getName(),
          Stream.concat(
                  q.getArgs().stream().limit(offset),
                  Stream.concat(Stream.of(ticket), q.getArgs().stream().skip(offset + 1)))
              .toList());
    }
    return q;
  }
}
