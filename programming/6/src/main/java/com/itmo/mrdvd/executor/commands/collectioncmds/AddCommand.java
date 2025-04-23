package com.itmo.mrdvd.executor.commands.collectioncmds;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.executor.commands.Command;
import java.util.Optional;

public class AddCommand<T extends HavingId> implements Command {
  protected final CollectionWorker<T, ?> collect;
  protected final InteractiveBuilder<T, ?> builder;

  public AddCommand(CollectionWorker<T, ?> collection, InteractiveBuilder<T, ?> builder) {
    this.collect = collection;
    this.builder = builder;
  }

  @Override
  public void execute() throws RuntimeException {
    Optional<T> obj = this.builder.build();
    if (obj.isEmpty() || collect.add(obj.get()).isEmpty()) {
      throw new RuntimeException("Не удалось добавить билет в коллекцию.");
    }
  }

  @Override
  public String name() {
    return "add";
  }
}
