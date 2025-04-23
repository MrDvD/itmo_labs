package com.itmo.mrdvd.executor.queries;

import java.util.List;

public class AddQuery extends QueryWithParams {
  public AddQuery(List<String> params) {
    super("add", "add {element}", "добавить новый элемент в коллекцию", params);
  }
}
