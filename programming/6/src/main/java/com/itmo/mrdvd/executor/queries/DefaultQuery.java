package com.itmo.mrdvd.executor.queries;

public class DefaultQuery implements Query {
  private final String cmd;
  private final String[] params;

  public DefaultQuery(String cmd, String[] params) {
    this.cmd = cmd;
    this.params = params;
  }

  @Override
  public String cmd() {
    return this.cmd;
  }

  @Override
  public String[] params() {
    return this.params;
  }
}
