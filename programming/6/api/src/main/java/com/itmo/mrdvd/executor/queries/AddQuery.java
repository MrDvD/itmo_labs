package com.itmo.mrdvd.executor.queries;

import java.util.List;

public class AddQuery<T> extends AbstractQuery {
  public AddQuery(List<Object> params) {
    super("add", "add {element}", "добавить новый элемент в коллекцию", params);
  }
}
