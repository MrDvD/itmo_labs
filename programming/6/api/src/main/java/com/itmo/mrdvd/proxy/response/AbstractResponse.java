package com.itmo.mrdvd.proxy.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
public abstract class AbstractResponse implements Response {
  protected String name;
  protected List<Object> body;

  public AbstractResponse(String name, List<Object> body) {
    this.name = name;
    this.body = body;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setBody(List<Object> body) {
    this.body = body;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public List<Object> getBody() {
    return this.body;
  }
}
