package com.itmo.mrdvd.executor.commands.response;

public abstract class AbstractResponse implements Response {
  protected String name;
  protected String body;
  
  public AbstractResponse(String name, String body) {
    this.name = name;
    this.body = body;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getName() {
    return this.name;
  }

  public String getBody() {
    return this.body;
  }
}
