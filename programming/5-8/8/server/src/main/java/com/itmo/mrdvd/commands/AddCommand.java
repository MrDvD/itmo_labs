package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.util.List;

public class AddCommand<T> implements Command<Void> {
  protected final CrudWorker<T, ?, ?> collect;
  protected final Validator<T> validator;
  protected final Class<T> clz;
  protected List<?> params;

  public AddCommand(CrudWorker<T, ?, ?> collection, Validator<T> validator, Class<T> clz) {
    this.collect = collection;
    this.validator = validator;
    this.clz = clz;
  }

  @Override
  public Void execute(List<Object> params) {
    if (this.collect == null) {
      throw new IllegalStateException("Не задана коллекция для работы.");
    }
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Не предоставлен объект для добавления в коллекцию.");
    }
    try {
      T obj = this.clz.cast(params.get(0));
      if (collect.add(obj, (T t) -> this.validator.validate(t)).isEmpty()) {
        throw new RuntimeException("Не удалось добавить элемент в коллекцию.");
      }
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать переданный объект.");
    }
    return null;
  }

  @Override
  public String name() {
    return "add";
  }

  @Override
  public String signature() {
    return name() + " {element}";
  }

  @Override
  public String description() {
    return "добавить элемент в коллекцию";
  }
}
