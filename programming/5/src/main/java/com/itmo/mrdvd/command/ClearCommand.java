package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.shell.Shell;

public class ClearCommand implements Command {
  private final CollectionWorker<?, ?> collection;
  private final Shell<?, ?> shell;

  public ClearCommand(CollectionWorker<?, ?> collect) {
    this(collect, null);
  }

  public ClearCommand(CollectionWorker<?, ?> collect, Shell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public ClearCommand setShell(Shell<?, ?> shell) {
    return new ClearCommand(collection, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(shell);
  }

  @Override
  public void execute() throws NullPointerException {
    collection.clear();
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    getShell().get().getOut().writeln("[INFO] Коллекция очищена.");
  }

  @Override
  public String name() {
    return "clear";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "очистить коллекцию";
  }
}
