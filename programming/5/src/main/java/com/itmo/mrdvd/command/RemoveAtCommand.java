package com.itmo.mrdvd.command;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.IntInputDevice;

public class RemoveAtCommand<T extends HavingId> implements CommandHasParams {
  private final CollectionWorker<T, List<T>> collection;
  private final IntInputDevice in;
  private final OutputDevice out;

  public RemoveAtCommand(CollectionWorker<T, List<T>> collection, IntInputDevice in, OutputDevice out) {
    this.collection = collection;
    this.in = in;
    this.out = out;
  }

  @Override
  public IntInputDevice getParamsInput() {
    return this.in;
  }

  @Override
  public void execute() {
    Optional<Integer> params = getParamsInput().readInt();
    getParamsInput().skipLine();
    if (params.isEmpty()) {
      out.writeln("[ERROR] Неправильный формат ввода: index должен быть целым неотрицательным числом.");
      return;
    }
    try {
      collection.getCollection().remove(params.get().intValue());
    } catch (IndexOutOfBoundsException e) {
      out.writeln("[WARN] В коллекции нет элемента с введённым параметром index.");
    }
  }

  @Override
  public String name() {
    return "remove_at";
  }

  @Override
  public String signature() {
    return name() + " index";
  }

  @Override
  public String description() {
    return "удалить элемент, находящийся в заданной позиции коллекции (index)";
  }
}
