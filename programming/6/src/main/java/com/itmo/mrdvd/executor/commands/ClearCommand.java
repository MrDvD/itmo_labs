package com.itmo.mrdvd.executor.commands;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.shell.ProxyShell;
import java.util.Optional;

public class ClearCommand implements Command {
  private final CollectionWorker<?, ?> collection;
  private final ProxyShell<?, ?> shell;

  public ClearCommand(CollectionWorker<?, ?> collect) {
    this(collect, null);
  }

  public ClearCommand(CollectionWorker<?, ?> collect, ProxyShell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public ClearCommand setShell(ProxyShell<?, ?> shell) {
    return new ClearCommand(collection, shell);
  }

  @Override
  public Optional<ProxyShell<?, ?>> getShell() {
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
