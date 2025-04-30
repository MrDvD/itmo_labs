package com.itmo.mrdvd.proxy;

import java.util.List;

public interface Query {
  public void setCmd(String cmd);

  public void setSignature(String signature);

  public void setDesc(String desc);

  public void setArgs(List<Object> params);

  public String getCmd();

  public String getSignature();

  public String getDesc();

  public List<Object> getArgs();
}
