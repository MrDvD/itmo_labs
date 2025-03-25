package com.itmo.mrdvd.command;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.shell.Shell;

public class AddIfCommand<T extends HavingId> implements Command {
  protected final CollectionWorker<T, List<T>> collect;
  protected final InteractiveBuilder<T> builder;
  protected final Shell<?, ?> shell;
  protected final Comparator<T> comparator;
  protected final Set<Integer> values;

  public AddIfCommand(
      CollectionWorker<T, List<T>> collection,
      InteractiveBuilder<T> builder,
      Comparator<T> comparator,
      Set<Integer> values) {
    this(collection, builder, comparator, values, null);
  }

  public AddIfCommand(
      CollectionWorker<T, List<T>> collection,
      InteractiveBuilder<T> builder,
      Comparator<T> comparator,
      Set<Integer> values,
      Shell<?, ?> shell) {
    this.collect = collection;
    this.builder = builder;
    this.shell = shell;
    this.comparator = comparator;
    this.values = values;
  }

  @Override
  public AddIfCommand<T> setShell(Shell<?, ?> shell) {
    return new AddIfCommand<>(collect, builder, comparator, values, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(shell);
  }

  @Override
  public void execute() throws NullPointerException {
    Optional<T> result = collect.add(builder, comparator, values);
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    if (result.isPresent()) {
      getShell().get().getOut().writeln("[INFO] Билет успешно добавлен в коллекцию.");
    } else {
      getShell().get().getOut().writeln("[INFO] Билет не добавлен в коллекцию: он не удовлетворяет условию.");
    }
  }

  @Override
  public String name() {
    return "add_if_max";
  }

  @Override
  public String signature() {
    return name() + " {element}";
  }

  @Override
  public String description() {
    return "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
  }
}
