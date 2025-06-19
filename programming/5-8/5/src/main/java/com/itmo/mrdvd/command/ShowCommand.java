package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public class ShowCommand implements Command {
  private final Collection<?, ?> collection;
  private final Shell<?, ?> shell;

  public ShowCommand(Collection<?, ?> collect) {
    this(collect, null);
  }

  public ShowCommand(Collection<?, ?> collect, Shell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public ShowCommand setShell(Shell<?, ?> shell) {
    return new ShowCommand(collection, shell);
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
