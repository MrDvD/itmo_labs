package com.itmo.mrdvd.executor.queries;

public class AddQuery<T> extends AbstractQuery {
  public AddQuery(Object params) {
    super("add", "add {element}", "добавить новый элемент в коллекцию", params);
  }
}
