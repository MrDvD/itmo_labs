package com.itmo.mrdvd.proxy.serviceQuery;

import java.util.List;

public class EmptyServiceQuery extends AbstractServiceQuery {
  public EmptyServiceQuery() {
    super("empty_service_query", List.of());
  }
}
