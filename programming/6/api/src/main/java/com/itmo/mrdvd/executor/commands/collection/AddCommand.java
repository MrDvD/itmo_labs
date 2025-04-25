package com.itmo.mrdvd.executor.commands.collection;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.builders.SuppliedBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.executor.commands.CommandWithParams;

public class AddCommand<T extends HavingId> implements CommandWithParams<Void> {
  protected final CollectionWorker<T, ?> collect;
  protected final SuppliedBuilder<T> builder;
  protected List<?> params;

  public AddCommand(CollectionWorker<T, ?> collection, SuppliedBuilder<T> builder) {
    this.collect = collection;
    this.builder = builder;
  }

  @Override
  public AddCommand<T> withParams(List<?> params) {
    this.params = params;
    return this;
  }

  @Override
  public Void execute() {
    Optional<T> obj = this.builder.withElements(params).build();
    if (obj.isEmpty() || collect.add(obj.get()).isEmpty()) {
      throw new RuntimeException("Не удалось добавить билет в коллекцию.");
    }
    return null;
  }

  @Override
  public String name() {
    return "add";
  }
}
