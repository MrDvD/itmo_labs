package com.itmo.mrdvd.executor.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.shell.DefaultShell;
import java.util.List;
import java.util.Optional;

public class RemoveLastCommand<T extends HavingId> implements Command {
  private final CollectionWorker<T, List<T>> collection;
  private final DefaultShell<?, ?> shell;

  public RemoveLastCommand(CollectionWorker<T, List<T>> collection) {
    this(collection, null);
  }

  public RemoveLastCommand(CollectionWorker<T, List<T>> collection, DefaultShell<?, ?> shell) {
    this.collection = collection;
    this.shell = shell;
  }

  @Override
  public RemoveLastCommand<T> setShell(DefaultShell<?, ?> shell) {
    return new RemoveLastCommand<>(collection, shell);
  }

  @Override
  public Optional<DefaultShell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    try {
      collection.getCollection().remove(collection.getCollection().size() - 1);
    } catch (IndexOutOfBoundsException e) {
      getShell().get().getOut().writeln("[WARN] Коллекция пуста.");
    }
  }

  @Override
  public String name() {
    return "remove_last";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "удалить последний элемент из коллекции";
  }
}
