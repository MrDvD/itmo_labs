package com.itmo.mrdvd.proxy.serviceQuery;

import java.util.List;

/*
 * DTO which is used between services for communication.
 */
public interface ServiceQuery {
  public String getName();

  public List<Object> getArgs();

  public static ServiceQuery of(String name, List<Object> args) {
    return new ServiceQuery() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public List<Object> getArgs() {
        return args;
      }
    };
  }
}
