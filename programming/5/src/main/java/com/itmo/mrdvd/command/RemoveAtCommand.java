package com.itmo.mrdvd.command;

import org.apache.commons.lang3.math.NumberUtils;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;

public class RemoveAtCommand implements Command {
  private final TicketCollection collection;
  private final OutputDevice out;

  public RemoveAtCommand(TicketCollection collect, OutputDevice out) {
    this.collection = collect;
    this.out = out;
  }

  public Integer parseIndex(String idxString) {
    int idx = NumberUtils.toInt(idxString, -1);
    return (idx >= 0 ? idx : null);
  }

  public int validateParams(String[] params) {
    if (params.length != 1) {
      return -3;
    }
    if (parseIndex(params[0]) == null) {
      return -1;
    }
    return 0;
  }

  @Override
  public void execute(String[] params) {
    int validationResult = validateParams(params);
    if (validationResult != 0) {
      switch (validationResult) {
        case -1 ->
            out.writeln(
                "[ERROR] Неправильный формат ввода: index должен быть целым неотрицательным числом.");
        default -> out.writeln("[ERROR] Неправильный формат ввода параметров команды.");
      }
      return;
    }
    int idx = parseIndex(params[0]);
    try {
      collection.getCollection().remove(idx);
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
