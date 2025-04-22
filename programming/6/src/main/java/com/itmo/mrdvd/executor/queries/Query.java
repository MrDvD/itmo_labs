package com.itmo.mrdvd.executor.queries;

public abstract class Query {
  private String cmd;
  private String signature;
  private String description;

  public Query(String cmd, String signature, String desc) {
    this.cmd = cmd;
    this.signature = signature;
    this.description = desc;
  }

  /** Just setter for deserialization purposes. */
  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  /** Just setter for deserialization purposes. */
  public void setSignature(String signature) {
    this.signature = signature;
  }

  /** Just setter for deserialization purposes. */
  public void setDesc(String desc) {
    this.description = desc;
  }

  public String getCmd() {
    return this.cmd;
  }

  public String getSignature() {
    return this.signature;
  }

  public String getDesc() {
    return this.description;
  }
}
