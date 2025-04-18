package com.itmo.mrdvd.executor.queries;

import java.util.List;

public interface QueryWithParams extends Query {
  public List<String> params();
}
