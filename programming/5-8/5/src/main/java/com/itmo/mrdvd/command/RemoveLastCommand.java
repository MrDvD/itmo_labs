package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.object.HavingId;
import com.itmo.mrdvd.shell.Shell;
import java.util.List;
import java.util.Optional;

public class RemoveLastCommand<T extends HavingId> implements Command {
  private final CollectionWorker<T, List<T>> collection;
  private final Shell<?, ?> shell;

  public RemoveLastCommand(CollectionWorker<T, List<T>> collection) {
    this(collection, null);
  }

  public RemoveLastCommand(CollectionWorker<T, List<T>> collection, Shell<?, ?> shell) {
    this.collection = collection;
    this.shell = shell;
  }

  @Override
  public RemoveLastCommand<T> setShell(Shell<?, ?> shell) {
    return new RemoveLastCommand<>(collection, shell);
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
