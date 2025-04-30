package com.itmo.mrdvd.queries;

import com.itmo.mrdvd.proxy.AbstractQuery;

public class UserQuery extends AbstractQuery {
  public UserQuery(String cmd) {
    super(cmd, cmd, "пользовательский запрос");
  }
}
