package com.itmo.mrdvd.executor.queries;

import java.util.List;

public interface Query {
  public String cmd();

  public List<String> params();
}
