package com.itmo.mrdvd.proxy.service_query;

import java.util.List;

/*
 * DTO which is used between services for communication.
 */
public abstract class AbstractServiceQuery implements ServiceQuery {
  private String name;
  private List<Object> args;

  public AbstractServiceQuery(String name, List<Object> args) {
    this.name = name;
    this.args = args;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setArgs(List<Object> args) {
    this.args = args;
  }

  @Override
  public List<Object> getArgs() {
    return this.args;
  }
}
