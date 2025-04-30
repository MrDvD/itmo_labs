package com.itmo.mrdvd.queries;

import com.itmo.mrdvd.proxy.AbstractQuery;

public class FetchAllQuery extends AbstractQuery {
  public FetchAllQuery() {
    super("fetch_all", "fetch_all", "получить доступные запросы от сервера");
  }
}
