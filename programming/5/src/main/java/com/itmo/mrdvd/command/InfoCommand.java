package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.device.OutputDevice;

public class InfoCommand implements Command {
  private final Collection<?, ?> collection;
  private final OutputDevice out;

  public InfoCommand(Collection<?, ?> collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute() {
    out.writeln(collection.getMetadata().toString());
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
