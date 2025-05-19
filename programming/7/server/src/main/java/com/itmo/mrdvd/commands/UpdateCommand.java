package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.proxy.UpdateDTO;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.util.List;

public class UpdateCommand<T> implements Command<Void> {
  private final CrudWorker<T, ?, Long> collect;
  private final Validator<T> validator;

  public UpdateCommand(CrudWorker<T, ?, Long> collection, Validator<T> validator) {
    this.collect = collection;
    this.validator = validator;
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
      UpdateDTO<T> obj = (UpdateDTO) params.get(0);
      if (collect
          .update(obj.getId(), obj.getObject(), (T t) -> this.validator.validate(t))
          .isEmpty()) {
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
