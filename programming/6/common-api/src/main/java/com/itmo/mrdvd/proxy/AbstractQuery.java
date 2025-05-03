package com.itmo.mrdvd.proxy;

import java.util.List;

public abstract class AbstractQuery implements Query {
  private String cmd;
  private String signature;
  private String description;
  private List<Object> params;

  public AbstractQuery(String cmd, String signature, String desc) {
    this(cmd, signature, desc, List.of());
  }

  public AbstractQuery(String cmd, String signature, String desc, List<Object> params) {
    this.cmd = cmd;
    this.signature = signature;
    this.description = desc;
    this.params = params;
  }

  @Override
  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  @Override
  public void setSignature(String signature) {
    this.signature = signature;
  }

  @Override
  public void setDesc(String desc) {
    this.description = desc;
  }

  @Override
  public void setArgs(List<Object> params) {
    this.params = params;
  }

  @Override
  public String getCmd() {
    return this.cmd;
  }

  @Override
  public String getSignature() {
    return this.signature;
  }

  @Override
  public String getDesc() {
    return this.description;
  }

  @Override
  public List<Object> getArgs() {
    return this.params;
  }
}
