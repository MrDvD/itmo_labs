package com.itmo.mrdvd.executor.queries;

public class FetchAllQuery extends Query {
  public FetchAllQuery() {
    super("fetch_all", "fetch_all", "получить доступные запросы к коллекции");
  }
}
