package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.collection.CollectionWorker;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public class AddCommand<T> implements Command {
  protected final CollectionWorker<T,?> collect;
  protected final InteractiveInputDevice in;
  protected final InteractiveBuilder<T> builder;
  protected final OutputDevice out;

  public AddCommand(CollectionWorker<T,?> collection, InteractiveBuilder<T> builder, InteractiveInputDevice in, OutputDevice out) {
    this.collect = collection;
    this.builder = builder;
    this.in = in;
    this.out = out;
  }

  @Override
  public void execute() {
    Optional<T> result = collect.add(builder);
    if (result.isPresent()) {
      out.writeln("[INFO] Билет успешно добавлен в коллекцию.");
    } else {
      out.writeln("[ERROR] Не удалось добавить билет в коллекцию.");
    }
  }

  @Override
  public String name() {
    return "add";
  }

  @Override
  public String signature() {
    return name() + " {element}";
  }

  @Override
  public String description() {
    return "добавить новый элемент в коллекцию";
  }
}
