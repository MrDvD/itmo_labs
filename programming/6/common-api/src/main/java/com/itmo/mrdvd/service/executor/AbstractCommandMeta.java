package com.itmo.mrdvd.service.executor;

/** DTO for keeping information about available commands. */
public abstract class AbstractCommandMeta implements CommandMeta {
  private String cmd;
  private String signature;
  private String description;

  public AbstractCommandMeta(String cmd, String signature, String desc) {
    this.cmd = cmd;
    this.signature = signature;
    this.description = desc;
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
}
