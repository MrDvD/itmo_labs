package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.util.List;

public class UpdateCommand<T extends HavingId> implements Command<Void> {
  private final CollectionWorker<T, ?> collect;
  private final Validator<T> validator;
  private final Class<T> clz;

  public UpdateCommand(CollectionWorker<T, ?> collection, Validator<T> validator, Class<T> clz) {
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
      throw new IllegalArgumentException("Не предоставлен объект для обновления в коллекции.");
    }
    try {
      T obj = this.clz.cast(params.get(0));
      if (collect.update(obj.getId(), obj, validator).isEmpty()) {
        throw new RuntimeException("Не удалось обновить элемент в коллекции.");
      }
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать переданный объект.");
    }
    return null;
  }

  @Override
  public String name() {
    return "update";
  }

  @Override
  public String signature() {
    return name() + " id {element}";
  }

  @Override
  public String description() {
    return "обновить значение элемента коллекции, id которого равен заданному";
  }
}
