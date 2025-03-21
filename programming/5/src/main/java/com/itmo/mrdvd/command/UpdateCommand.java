package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.LongInputDevice;

public class UpdateCommand<T extends HavingId> implements CommandHasParams {
  private final CollectionWorker<T,?> collect;
  private final InteractiveUpdater<T> updater;
  private final LongInputDevice in;
  private final OutputDevice out;

  public UpdateCommand(CollectionWorker<T,?> collection, InteractiveUpdater<T> updater, LongInputDevice in, OutputDevice out) {
    this.collect = collection;
    this.updater = updater;
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
    Optional<?> result = collect.update(params.get(), updater);
    if (result.isEmpty()) {
      out.writeln("[ERROR] Элемент с таким id не найден.");
    } else {
      out.writeln("[INFO] Объект успешно обновлён в коллекции.");
    }
    
  }

  @Override
  public String name() {
    return "update";
  }

  @Override
  public String signature() {
    return name() + " id {element}";
  }

  @Override
  public String description() {
    return "обновить значение элемента коллекции, id которого равен заданному";
  }
}
