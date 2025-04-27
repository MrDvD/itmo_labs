package com.itmo.mrdvd.executor.queries;

public class FetchAllQuery extends AbstractQuery {
  public FetchAllQuery() {
    super("fetch_all", "fetch_all", "получить доступные запросы к коллекции");
  }
}
