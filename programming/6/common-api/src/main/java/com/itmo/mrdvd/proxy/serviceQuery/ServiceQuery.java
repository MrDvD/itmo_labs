package com.itmo.mrdvd.proxy.serviceQuery;

import java.util.List;

public interface ServiceQuery {
  public void setName(String name);

  public String getName();

  public void setArgs(List<Object> args);

  public List<Object> getArgs();
}
