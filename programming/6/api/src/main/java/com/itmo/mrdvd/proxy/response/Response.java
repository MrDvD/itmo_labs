package com.itmo.mrdvd.proxy.response;

public interface Response {
  public void setName(String name);

  public void setBody(Object body);

  public String getName();

  public Object getBody();
}
