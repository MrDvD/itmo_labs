package com.itmo.mrdvd.proxy.serviceQuery;

import java.util.List;

public class EmptyServiceQuery implements ServiceQuery {
  @Override
  public String getName() {
    return "empty_query";
  }

  @Override
  public List<Object> getArgs() {
    return List.of();
  }
}
