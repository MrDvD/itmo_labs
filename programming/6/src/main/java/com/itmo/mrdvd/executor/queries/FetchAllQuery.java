package com.itmo.mrdvd.executor.queries;

import java.util.List;

public class FetchAllQuery implements Query {
  @Override
  public String cmd() {
    return "fetch_all";
  }

  @Override
  public List<String> params() {
    return List.of();
  }
}
