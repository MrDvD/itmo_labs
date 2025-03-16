package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.device.OutputDevice;

public class ClearCommand implements Command {
  private final CollectionWorker<?, ?> collection;
  private final OutputDevice out;

  public ClearCommand(CollectionWorker<?, ?> collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute() {
    collection.clear();
    out.writeln("[INFO] Коллекция очищена.");
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
