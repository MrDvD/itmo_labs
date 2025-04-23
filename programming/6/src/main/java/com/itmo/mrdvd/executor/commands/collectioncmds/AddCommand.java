package com.itmo.mrdvd.executor.commands.collectioncmds;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.executor.commands.Command;
import java.util.Optional;

public class AddCommand<T extends HavingId> implements Command {
  protected final CollectionWorker<T, ?> collect;
  protected final InteractiveBuilder builder;

  public AddCommand(CollectionWorker<T, ?> collection, InteractiveBuilder<T, ?> builder) {
    this(collection, builder, null);
  }

  public AddCommand(CollectionWorker<T, ?> collection, InteractiveBuilder builder) {
    this.collect = collection;
    this.builder = builder.setIn(shell.getIn());
  }

  @Override
  public void execute() throws RuntimeException {
    Optional<T> result = collect.add(builder);
    if (result.isEmpty()) {
      throw new RuntimeException("[ERROR] Не удалось добавить билет в коллекцию.");
    }
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
    return "добавить новый элемент в коллекцию";
  }
}
