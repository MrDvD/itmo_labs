package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.device.OutputDevice;

public class ShowCommand<T> implements Command {
  private final Collection<T, ?> collection;
  private final OutputDevice out;

  public ShowCommand(Collection<T, ?> collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute() {
    for (T ticket : collection) {
      out.writeln(ticket.toString());
    }
    out.writeln("[INFO] Конец коллекции.");
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
