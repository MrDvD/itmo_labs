package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.object.HavingId;
import com.itmo.mrdvd.shell.Shell;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MinByPriceCommand<T extends HavingId> implements Command {
  private final CollectionWorker<T, List<T>> collection;
  private final Shell<?, ?> shell;
  private final Comparator<T> comparator;

  public MinByPriceCommand(CollectionWorker<T, List<T>> collect, Comparator<T> comparator) {
    this(collect, comparator, null);
  }

  public MinByPriceCommand(
      CollectionWorker<T, List<T>> collect, Comparator<T> comparator, Shell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
    this.comparator = comparator;
  }

  @Override
  public MinByPriceCommand<T> setShell(Shell<?, ?> shell) {
    return new MinByPriceCommand<>(collection, comparator, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    collection.getCollection().sort(comparator);
    if (collection.getCollection().isEmpty()) {
      getShell().get().getOut().writeln("[INFO] Коллекция пуста.");
    } else {
      getShell().get().getOut().writeln(collection.getCollection().get(0).toString());
    }
  }

  @Override
  public String name() {
    return "min_by_price";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести любой объект из коллекции, значение поля price которого является минимальным";
  }
}
