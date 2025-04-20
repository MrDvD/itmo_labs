package com.itmo.mrdvd.executor.queries;

public abstract class Query {
  private String cmd;

  public Query(String cmd) {
    this.cmd = cmd;
  }

  /** Just setter for deserialization purposes. */
  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  public String getCmd() {
    return this.cmd;
  }
}
