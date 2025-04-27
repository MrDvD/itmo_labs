package com.itmo.mrdvd.executor.commands.response;

public interface Response {
  public void setName(String name);

  public void setBody(String body);

  public String getName();

  public String getBody();
}
