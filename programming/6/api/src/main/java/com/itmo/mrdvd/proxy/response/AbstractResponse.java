package com.itmo.mrdvd.proxy.response;

public abstract class AbstractResponse implements Response {
  protected String name;
  protected Object body;

  public AbstractResponse(String name, Object body) {
    this.name = name;
    this.body = body;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setBody(Object body) {
    this.body = body;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Object getBody() {
    return this.body;
  }
}
