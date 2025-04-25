package com.itmo.mrdvd.executor.commands.todo;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.shell.ProxyShell;
import java.util.Optional;

public class ShowCommand implements Command {
  private final Collection<?, ?> collection;
  private final ProxyShell<?, ?> shell;

  public ShowCommand(Collection<?, ?> collect) {
    this(collect, null);
  }

  public ShowCommand(Collection<?, ?> collect, ProxyShell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public ShowCommand setShell(ProxyShell<?, ?> shell) {
    return new ShowCommand(collection, shell);
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
