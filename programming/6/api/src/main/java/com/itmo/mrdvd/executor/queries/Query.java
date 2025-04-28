package com.itmo.mrdvd.executor.queries;

public interface Query {
  public void setCmd(String cmd);

  public void setSignature(String signature);

  public void setDesc(String desc);

  public void setArgs(Object params);

  public String getCmd();

  public String getSignature();

  public String getDesc();

  public Object getArgs();
}
