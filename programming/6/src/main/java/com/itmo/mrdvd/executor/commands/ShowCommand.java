package com.itmo.mrdvd.executor.command;

import java.util.Optional;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.shell.DefaultShell;

public class ShowCommand implements Command {
  private final Collection<?, ?> collection;
  private final DefaultShell<?, ?> shell;

  public ShowCommand(Collection<?, ?> collect) {
    this(collect, null);
  }

  public ShowCommand(Collection<?, ?> collect, DefaultShell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public ShowCommand setShell(DefaultShell<?, ?> shell) {
    return new ShowCommand(collection, shell);
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
    for (Object obj : collection) {
      getShell().get().getOut().writeln(obj.toString());
    }
    getShell().get().getOut().writeln("[INFO] Конец коллекции.");
  }

  @Override
  public String name() {
    return "show";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
  }
}
