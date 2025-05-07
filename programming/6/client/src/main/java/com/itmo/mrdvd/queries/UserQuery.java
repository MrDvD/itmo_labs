package com.itmo.mrdvd.queries;

import com.itmo.mrdvd.proxy.DefaultQuery;

public class UserQuery extends DefaultQuery {
  public UserQuery(String cmd) {
    super(cmd, cmd, "пользовательский запрос");
  }
}
