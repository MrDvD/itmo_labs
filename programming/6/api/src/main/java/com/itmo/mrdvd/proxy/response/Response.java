package com.itmo.mrdvd.proxy.response;

import java.util.List;

public interface Response {
  public void setName(String name);

  public void setBody(List<Object> body);

  public String getName();

  public List<Object> getBody();
}
