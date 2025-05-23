package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.shell.Shell;
import java.util.Optional;

public class InfoCommand implements Command {
  private final Collection<?, ?> collection;
  private final Shell<?, ?> shell;

  public InfoCommand(Collection<?, ?> collect) {
    this(collect, null);
  }

  public InfoCommand(Collection<?, ?> collect, Shell<?, ?> shell) {
    this.collection = collect;
    this.shell = shell;
  }

  @Override
  public InfoCommand setShell(Shell<?, ?> shell) {
    return new InfoCommand(collection, shell);
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
    getShell().get().getOut().writeln(collection.getMetadata().toString());
  }

  @Override
  public String name() {
    return "info";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести в стандартный поток вывода информацию о коллекции";
  }
}
