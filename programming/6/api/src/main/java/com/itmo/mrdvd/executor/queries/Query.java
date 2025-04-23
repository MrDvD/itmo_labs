package com.itmo.mrdvd.executor.queries;

import java.util.List;

public abstract class Query {
  private String cmd;
  private String signature;
  private String description;
  private List<?> params;

  public Query(String cmd, String signature, String desc) {
    this(cmd, signature, desc, List.of());
  }

  public Query(String cmd, String signature, String desc, List<?> params) {
    this.cmd = cmd;
    this.signature = signature;
    this.description = desc;
    this.params = params;
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

  /** Just setter for deserialization purposes. */
  public void setParams(List<String> params) {
    this.params = params;
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

  public List<?> getParams() {
    return this.params;
  }
}
