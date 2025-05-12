package com.itmo.mrdvd.service.executor;

public interface CommandMeta {
  public void setCmd(String cmd);

  public void setSignature(String signature);

  public void setDesc(String desc);

  public String getCmd();

  public String getSignature();

  public String getDesc();
}
