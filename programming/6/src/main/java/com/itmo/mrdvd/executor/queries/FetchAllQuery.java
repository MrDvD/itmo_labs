package com.itmo.mrdvd.executor.queries;

public class FetchAllQuery implements Query {
  @Override
  public String cmd() {
    return "fetch_all";
  }
}
