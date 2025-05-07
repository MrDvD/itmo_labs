package com.itmo.mrdvd.queries;

import com.itmo.mrdvd.proxy.DefaultQuery;

public class FetchAllQuery extends DefaultQuery {
  public FetchAllQuery() {
    super("fetch_all", "fetch_all", "получить доступные запросы от сервера");
  }
}
