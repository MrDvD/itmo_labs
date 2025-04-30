package com.itmo.mrdvd.commands.todo;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.shell.ProxyShell;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RemoveAtCommand<T extends HavingId> implements Command {
  private final CollectionWorker<T, List<T>> collection;
  private final ProxyShell<?, ?> shell;

  public RemoveAtCommand(CollectionWorker<T, List<T>> collection) {
    this(collection, null);
  }

  public RemoveAtCommand(CollectionWorker<T, List<T>> collection, ProxyShell<?, ?> shell) {
    this.collection = collection;
    this.shell = shell;
  }

  @Override
  public RemoveAtCommand<T> setShell(ProxyShell<?, ?> shell) {
    return new RemoveAtCommand<>(collection, shell);
  }

  @Override
  public Optional<ProxyShell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    Optional<Integer> params = Optional.empty();
    try {
      params = getShell().get().getIn().readInt();
    } catch (IOException e) {
    }
    getShell().get().getIn().skipLine();
    if (params.isEmpty()) {
      getShell()
          .get()
          .getOut()
          .writeln(
              "[ERROR] Неправильный формат ввода: index должен быть целым неотрицательным числом.");
      return;
    }
    try {
      collection.getCollection().remove(params.get().intValue());
    } catch (IndexOutOfBoundsException e) {
      getShell()
          .get()
          .getOut()
          .writeln("[WARN] В коллекции нет элемента с введённым параметром index.");
    }
  }

  @Override
  public String name() {
    return "remove_at";
  }

  @Override
  public String signature() {
    return name() + " index";
  }

  @Override
  public String description() {
    return "удалить элемент, находящийся в заданной позиции коллекции (index)";
  }

  @Override
  public boolean hasParams() {
    return true;
  }
}
