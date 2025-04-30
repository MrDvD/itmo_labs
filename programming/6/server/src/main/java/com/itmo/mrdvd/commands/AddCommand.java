package com.itmo.mrdvd.commands;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.builders.SuppliedBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;

public class AddCommand<T extends HavingId> implements Command<Void> {
  protected final CollectionWorker<T, ?> collect;
  protected final SuppliedBuilder<T> builder;
  protected List<?> params;

  public AddCommand(CollectionWorker<T, ?> collection, SuppliedBuilder<T> builder) {
    this.collect = collection;
    this.builder = builder;
  }

  @Override
  public Void execute(List<Object> params) {
    Optional<T> obj = this.builder.withElements(params).build();
    if (obj.isEmpty() || collect.add(obj.get()).isEmpty()) {
      throw new RuntimeException("Не удалось добавить элемент в коллекцию.");
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
