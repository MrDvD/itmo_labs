package com.itmo.mrdvd.command;

import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.object.HavingId;
import com.itmo.mrdvd.shell.Shell;
import java.io.IOException;
import java.util.Optional;

public class UpdateCommand<T extends HavingId> implements Command {
  private final CollectionWorker<T, ?> collect;
  private final InteractiveUpdater updater;
  private final Shell<?, ?> shell;

  public UpdateCommand(CollectionWorker<T, ?> collection, InteractiveUpdater<T, ?> updater) {
    this(collection, updater, null);
  }

  public UpdateCommand(
      CollectionWorker<T, ?> collection, InteractiveUpdater updater, Shell<?, ?> shell) {
    this.collect = collection;
    this.updater = shell == null ? updater : updater.setIn(shell.getIn());
    this.shell = shell;
  }

  @Override
  public UpdateCommand<T> setShell(Shell<?, ?> shell) {
    return new UpdateCommand<>(collect, updater, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(shell);
  }

  @Override
  public void execute() {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    Optional<Long> params = Optional.empty();
    try {
      params = getShell().get().getIn().readLong();
    } catch (IOException e) {
    }
    getShell().get().getIn().skipLine();
    if (params.isEmpty()) {
      getShell()
          .get()
          .getOut()
          .writeln("[ERROR] Неправильный формат ввода: id должен быть целым числом.");
      return;
    }
    Optional<?> result = collect.update(params.get(), updater);
    if (result.isEmpty()) {
      getShell().get().getOut().writeln("[ERROR] Элемент с таким id не найден.");
    } else {
      getShell().get().getOut().writeln("[INFO] Объект успешно обновлён в коллекции.");
    }
  }

  @Override
  public String name() {
    return "update";
  }

  @Override
  public String signature() {
    return name() + " id {element}";
  }

  @Override
  public String description() {
    return "обновить значение элемента коллекции, id которого равен заданному";
  }

  @Override
  public boolean hasParams() {
    return true;
  }
}
