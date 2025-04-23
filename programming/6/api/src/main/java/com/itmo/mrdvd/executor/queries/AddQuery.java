package com.itmo.mrdvd.executor.queries;

import java.util.List;

public class AddQuery<T> extends Query {
  public AddQuery(List<T> params) {
    super("add", "add {element}", "добавить новый элемент в коллекцию", params);
  }
}
