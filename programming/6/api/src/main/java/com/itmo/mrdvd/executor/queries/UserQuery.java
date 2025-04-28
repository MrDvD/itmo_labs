package com.itmo.mrdvd.executor.queries;

public class UserQuery extends AbstractQuery {
  public UserQuery(String cmd) {
    super(cmd, cmd, "пользовательский запрос");
  }
}
