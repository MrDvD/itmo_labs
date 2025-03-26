package com.itmo.mrdvd.command;

import java.io.IOException;
import java.util.Optional;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.shell.Shell;

public class RemoveByIdCommand implements Command {
  private final CollectionWorker<?, ?> collection;
  private final Shell<?, ?> shell;

  public RemoveByIdCommand(CollectionWorker<?, ?> collection) {
    this(collection, null);
  }

  public RemoveByIdCommand(
      CollectionWorker<?, ?> collection, Shell<?, ?> shell) {
    this.collection = collection;
    this.shell = shell;
  }

  @Override
  public RemoveByIdCommand setShell(Shell<?, ?> shell) {
    return new RemoveByIdCommand(collection, shell);
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
    Optional<Long> params = Optional.empty();
    try {
      params = getShell().get().getIn().readLong();
    } catch (IOException e) {}
    getShell().get().getIn().skipLine();
    if (params.isEmpty()) {
      getShell().get().getOut().writeln("[ERROR] Неправильный формат ввода: id должен быть целым числом.");
      return;
    }
    collection.remove(params.get());
  }

  @Override
  public String name() {
    return "remove_by_id";
  }

  @Override
  public String signature() {
    return name() + " id";
  }

  @Override
  public String description() {
    return "удалить элемент из коллекции по его id";
  }

  @Override
  public boolean hasParams() {
    return true;
  }
}
