package com.itmo.mrdvd.executor;

public interface Query {
  public String cmd();
  public String[] params();
}
