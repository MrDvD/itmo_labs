package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.LongInputDevice;
import java.util.Optional;

public class RemoveByIdCommand implements CommandHasParams {
  private final CollectionWorker<?, ?> collection;
  private final LongInputDevice in;
  private final OutputDevice out;

  public RemoveByIdCommand(
      CollectionWorker<?, ?> collection, LongInputDevice in, OutputDevice out) {
    this.collection = collection;
    this.in = in;
    this.out = out;
  }

  @Override
  public LongInputDevice getParamsInput() {
    return this.in;
  }

  @Override
  public void execute() {
    Optional<Long> params = getParamsInput().readLong();
    getParamsInput().skipLine();
    if (params.isEmpty()) {
      out.writeln("[ERROR] Неправильный формат ввода: id должен быть целым числом.");
      return;
    }
    collection.remove(params.get());
  }

  @Override
  public String name() {
    return "remove_by_id";
  }

  @Override
  public String signature() {
    return name() + " id";
  }

  @Override
  public String description() {
    return "удалить элемент из коллекции по его id";
  }
}
