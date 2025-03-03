package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;

public class ClearCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;

  public ClearCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  @Override
  public void execute(String[] params) {
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
