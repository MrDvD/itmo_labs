package com.itmo.mrdvd.executor.queries;

public interface Query {
  public String cmd();

  public int paramsCount();

  public String[] params();
}
