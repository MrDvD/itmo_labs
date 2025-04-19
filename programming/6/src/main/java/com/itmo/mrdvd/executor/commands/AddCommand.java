package com.itmo.mrdvd.executor.commands;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.shell.ProxyShell;
import java.util.Optional;

public class AddCommand<T extends HavingId> implements Command {
  protected final CollectionWorker<T, ?> collect;
  protected final InteractiveBuilder builder;
  protected final ProxyShell<?, ?> shell;

  public AddCommand(CollectionWorker<T, ?> collection, InteractiveBuilder<T, ?> builder) {
    this(collection, builder, null);
  }

  public AddCommand(
      CollectionWorker<T, ?> collection, InteractiveBuilder builder, ProxyShell<?, ?> shell) {
    this.collect = collection;
    this.builder = shell == null ? builder : builder.setIn(shell.getIn());
    this.shell = shell;
  }

  @Override
  public AddCommand<T> setShell(ProxyShell<?, ?> shell) {
    return new AddCommand<>(collect, builder, shell);
  }

  @Override
  public Optional<ProxyShell<?, ?>> getShell() {
    return Optional.ofNullable(shell);
  }

  @Override
  public void execute() throws NullPointerException {
    Optional<T> result = collect.add(builder);
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    if (result.isPresent()) {
      getShell().get().getOut().writeln("[INFO] Билет успешно добавлен в коллекцию.");
    } else {
      shell.getOut().writeln("\n[ERROR] Не удалось добавить билет в коллекцию.");
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
